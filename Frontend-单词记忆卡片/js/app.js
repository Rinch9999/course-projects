(function () {
    'use strict';

    const STORAGE_KEY = 'word_cards_v2';
    const $ = (id) => document.getElementById(id);

    function loadJSON(key) {
        try { return JSON.parse(localStorage.getItem(key)) || []; }
        catch (e) { return []; }
    }

    function escapeHtml(str) {
        return String(str)
            .replace(/&/g, '&amp;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;')
            .replace(/"/g, '&quot;')
            .replace(/'/g, '&#39;');
    }

    // 载入并规范化数据（兼容无 id 的旧数据）
    let cards = loadJSON(STORAGE_KEY).map(c => ({
        id: String(c.id != null ? c.id : (Date.now() + Math.random())),
        word: String(c.word || ''),
        meaning: String(c.meaning || ''),
        memorized: !!c.memorized,
        hard: !!c.hard
    }));

    let mode = 'all';       // all | review | hard
    let currentIndex = -1;

    function save() { localStorage.setItem(STORAGE_KEY, JSON.stringify(cards)); }

    function visible() {
        if (mode === 'review') return cards.filter(c => !c.memorized);
        if (mode === 'hard') return cards.filter(c => c.hard);
        return cards;
    }

    function setMode(m) {
        mode = m;
        $('modeAll').classList.toggle('active', m === 'all');
        $('modeReview').classList.toggle('active', m === 'review');
        $('modeHard').classList.toggle('active', m === 'hard');
        currentIndex = -1;
        next();
    }

    function showCard(i) {
        const list = visible();
        if (!list.length) {
            $('cardFront').textContent = '暂无单词，先添加一个吧';
            $('cardBack').textContent = '';
            currentIndex = -1;
            return;
        }
        currentIndex = (i + list.length) % list.length;
        const c = list[currentIndex];
        $('cardFront').textContent = c.word;
        $('cardBack').textContent = c.meaning;
        $('card').classList.remove('flipped');
    }

    function flip() { $('card').classList.toggle('flipped'); }
    function next() { showCard(currentIndex + 1); }
    function prev() { showCard(currentIndex - 1); }

    function addCard() {
        const word = $('word').value.trim();
        const meaning = $('meaning').value.trim();
        if (!word || !meaning) { alert('请填写单词和释义'); return; }
        cards.push({ id: String(Date.now() + Math.random()), word, meaning, memorized: false, hard: false });
        save();
        $('word').value = ''; $('meaning').value = '';
        renderAll();
        showCard(visible().length - 1);
    }

    function mark(memorized) {
        const list = visible(); if (!list.length) return;
        list[currentIndex].memorized = memorized;
        save(); renderAll(); next();
    }

    function collectHard() {
        const list = visible(); if (!list.length) return;
        const c = list[currentIndex];
        c.hard = !c.hard;   // 再点一次取消收藏
        save(); renderAll();
    }

    function shuffle() {
        for (let i = cards.length - 1; i > 0; i--) {
            const j = Math.floor(Math.random() * (i + 1));
            [cards[i], cards[j]] = [cards[j], cards[i]];
        }
        save(); showCard(0);
    }

    function editCard(id) {
        const c = cards.find(x => x.id === id); if (!c) return;
        const word = prompt('修改单词', c.word);
        if (word === null) return;
        const meaning = prompt('修改释义', c.meaning);
        if (meaning === null) return;
        c.word = word.trim() || c.word;
        c.meaning = meaning.trim() || c.meaning;
        save(); renderAll(); showCard(currentIndex);
    }

    function deleteCard(id) {
        if (!confirm('确认删除该单词？')) return;
        cards = cards.filter(c => c.id !== id);
        save(); renderAll(); next();
    }

    function exportData() {
        const blob = new Blob([JSON.stringify(cards, null, 2)], { type: 'application/json' });
        const a = document.createElement('a');
        a.href = URL.createObjectURL(blob);
        a.download = 'word_cards.json';
        a.click();
        URL.revokeObjectURL(a.href);
    }

    function importData(input) {
        const file = input.files[0]; if (!file) return;
        const reader = new FileReader();
        reader.onload = function () {
            try {
                const arr = JSON.parse(reader.result);
                if (!Array.isArray(arr)) throw new Error('bad');
                cards = arr.map(c => ({
                    id: String(c.id != null ? c.id : (Date.now() + Math.random())),
                    word: String(c.word || ''),
                    meaning: String(c.meaning || ''),
                    memorized: !!c.memorized,
                    hard: !!c.hard
                }));
                save(); renderAll(); showCard(0);
            } catch (e) { alert('导入失败：文件格式不正确'); }
        };
        reader.readAsText(file);
        input.value = '';
    }

    function renderStats() {
        $('statTotal').textContent = cards.length;
        $('statMemorized').textContent = cards.filter(c => c.memorized).length;
        $('statRemain').textContent = cards.filter(c => !c.memorized).length;
        $('statHard').textContent = cards.filter(c => c.hard).length;
    }

    function renderList() {
        $('wordList').innerHTML = cards.length
            ? cards.map(c => `
                <li class="${c.memorized ? 'memorized' : ''}">
                    <span class="w">${escapeHtml(c.word)}</span>
                    <span class="m">${escapeHtml(c.meaning)}</span>
                    <span class="ops">
                        <button onclick="editCard('${c.id}')">编辑</button>
                        <button onclick="deleteCard('${c.id}')">删除</button>
                    </span>
                </li>`).join('')
            : '<li class="empty">还没有单词，先添加一个吧</li>';
    }

    function renderHard() {
        const hard = cards.filter(c => c.hard);
        $('hardList').innerHTML = hard.length
            ? hard.map(c => `<li><span class="w">${escapeHtml(c.word)}</span><span class="m">${escapeHtml(c.meaning)}</span></li>`).join('')
            : '<li class="empty">生词本为空</li>';
    }

    function renderAll() { renderStats(); renderList(); renderHard(); }

    // 键盘快捷键
    document.addEventListener('keydown', (e) => {
        if (e.target.tagName === 'INPUT') return;
        if (e.code === 'Space') { e.preventDefault(); flip(); }
        else if (e.key === 'ArrowLeft') prev();
        else if (e.key === 'ArrowRight') next();
    });

    // 暴露给内联 onclick
    window.setMode = setMode;
    window.addCard = addCard;
    window.flip = flip;
    window.prev = prev;
    window.next = next;
    window.mark = mark;
    window.collectHard = collectHard;
    window.shuffle = shuffle;
    window.editCard = editCard;
    window.deleteCard = deleteCard;
    window.exportData = exportData;
    window.importData = importData;

    renderAll();
    const firstUnmemorized = cards.findIndex(c => !c.memorized);
    showCard(firstUnmemorized === -1 ? 0 : firstUnmemorized);
})();