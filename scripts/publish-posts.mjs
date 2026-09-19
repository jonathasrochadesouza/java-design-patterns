#!/usr/bin/env node

import { readFileSync, writeFileSync, existsSync, readdirSync } from 'node:fs';
import { join } from 'node:path';

const DRY_RUN = process.argv.includes('--dry-run');
const DEVTO_ONLY = process.argv.includes('--devto-only');
const NOW = new Date();

function log(emoji, msg) { console.log(`${emoji} ${msg}`); }

function parseFrontMatter(content) {
  const match = content.match(/^---\n([\s\S]*?)\n---/);
  if (!match) return { meta: {}, body: content };
  const block = match[1];
  const meta = {};
  for (const line of block.split('\n')) {
    const kv = line.match(/^(\w[\w-]*):\s*(.+)/);
    if (kv) {
      let val = kv[2].trim();
      if (val.startsWith('[') && val.endsWith(']'))
        val = val.slice(1, -1).split(',').map(s => s.trim().replace(/^["']|["']$/g, ''));
      else val = val.replace(/^["']|["']$/g, '');
      meta[kv[1]] = val;
    }
  }
  const body = content.slice(match[0].length).trim();
  return { meta, body };
}

function updateFrontMatterField(content, key, value) {
  return content.replace(
    new RegExp(`^${key}:.*$`, 'm'),
    `${key}: "${value}"`
  );
}

async function publishDevTo(post, filePath, mediumUrl) {
  const apiKey = process.env.DEVTO_API_KEY;
  if (!apiKey) { log('❌', 'DEVTO_API_KEY not set'); return false; }

  const tags = Array.isArray(post.meta.tags) ? post.meta.tags : [];
  const res = await fetch('https://dev.to/api/articles', {
    method: 'POST',
    headers: {
      'api-key': apiKey,
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      article: {
        title: post.meta.title,
        published: true,
        body_markdown: post.body,
        tags: tags.slice(0, 5),
        canonical_url: mediumUrl || post.meta.canonical_url || undefined,
        cover_image: post.meta.cover_image || undefined
      }
    })
  });

  if (!res.ok) {
    const err = await res.text();
    log('❌', `dev.to publish failed: ${res.status} ${err}`);
    return false;
  }
  const data = await res.json();
  log('✅', `dev.to published: ${data.url}`);
  return data.url;
}

function loadPublishState() {
  const p = join(process.cwd(), '.publish-state.json');
  if (existsSync(p)) return JSON.parse(readFileSync(p, 'utf-8'));
  return {};
}

function savePublishState(state) {
  writeFileSync(join(process.cwd(), '.publish-state.json'), JSON.stringify(state, null, 2) + '\n');
}

async function main() {
  log('🚀', `Publish runner started at ${NOW.toISOString()} (dry run: ${DRY_RUN}, devto only: ${DEVTO_ONLY})`);
  const state = loadPublishState();
  const results = [];
  const dirs = DEVTO_ONLY ? ['dev-to'] : ['dev-to'];

  for (const dir of dirs) {
    const fullDir = join(process.cwd(), dir);
    if (!existsSync(fullDir)) continue;
    const files = readdirSync(fullDir).filter(f => f.endsWith('.md'));

    for (const file of files) {
      const filePath = join(fullDir, file);
      const raw = readFileSync(filePath, 'utf-8');
      const { meta, body } = parseFrontMatter(raw);

      if (meta.status === 'published') {
        log('⏭️', `${file} already published`);
        continue;
      }

      if (!meta.publishOn) {
        log('⚠️', `${file} has no publishOn — skipping`);
        continue;
      }

      const publishDate = new Date(meta.publishOn);
      if (publishDate > NOW) {
        log('⏳', `${file} scheduled for ${meta.publishOn} — not yet`);
        continue;
      }

      log('📤', `Publishing ${file}...`);
      let url = null;

      if (DRY_RUN) {
        log('🔍', `[DRY RUN] Would publish ${file}`);
        url = `dry-run-${file}`;
      } else {
        const mediumPrinciple = `medium-pt-${meta.principle}`;
        const mediumUrl = state[mediumPrinciple]?.url || '';
        url = await publishDevTo({ meta, body }, filePath, mediumUrl);
      }

      if (url) {
        state[`devto-pt-${meta.principle}`] = {
          url,
          publishedAt: NOW.toISOString()
        };
        if (!DRY_RUN) {
          const updated = updateFrontMatterField(raw, 'status', 'published');
          writeFileSync(filePath, updated);
        }
        results.push({ file, url });
      }
    }
  }

  if (!DRY_RUN) savePublishState(state);
  log('🏁', `Done. ${results.length} post(s) published.`);
  if (results.length > 0) results.forEach(r => log('  →', `${r.file}: ${r.url}`));
}

main().catch(err => { console.error(err); process.exit(1); });
