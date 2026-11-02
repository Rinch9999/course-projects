(function () {
    'use strict';

    const STORAGE_KEY = 'activity_signups_v2';
    const $ = (id) => document.getElementById(id);

    // 内置活动数据（模拟后端返回）
    const ACTIVITIES = [
        { id: 'a1', title: '校园歌手大赛', date: '2026-11-20 19:00', location: '大学生活动中心', quota: 100, desc: '面向全校的歌唱比赛，设一、二、三等奖及人气奖，欢迎报名参赛或到场观赛。' },
        { id: 'a2', title: '篮球友谊赛', date: '2026-11-25 15:00', location: '南操场篮球馆', quota: 40, desc: '院系间五人制篮球友谊赛，报名即有机会入选院队，为院争光。' },
        { id: 'a3', title: '24 小时编程马拉松', date: '2026-12-05 09:00', location: '图书馆信息中心', quota: 60, desc: '自由组队完成趣味挑战题，完赛队伍可获参与证明与精美奖品。' }
    ];

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

    let signups = loadJSON(STORAGE_KEY);
    let currentActivityId = ACTIVITIES[0].id;

    function save() { localStorage.setItem(STORAGE_KEY, JSON.stringify(signups)); }

    function currentActivity() { return ACTIVITIES.find(a => a.id === currentActivityId); }
    function currentSignups() { return signups.filter(s => s.activityId === currentActivityId); }

    function switchActivity(id) {
        currentActivityId = id;
        renderTabs(); renderDetail(); renderList();
    }

    function renderTabs() {
        $('tabs').innerHTML = ACTIVITIES.map(a =>
            `<button class="${a.id === currentActivityId ? 'active' : ''}" onclick="switchActivity('${a.id}')">${escapeHtml(a.title)}</button>`).join('');
    }

    function renderDetail() {
        const a = currentActivity();
        const left = Math.max(a.quota - currentSignups().length, 0);
        $('detail').innerHTML = `
            <h2>${escapeHtml(a.title)}</h2>
            <p class="desc">${escapeHtml(a.desc)}</p>
            <div class="meta">
                <span>时间：${escapeHtml(a.date)}</span>
                <span>地点：${escapeHtml(a.location)}</span>
                <span>剩余名额：${left} / ${a.quota}</span>
            </div>`;
    }

    function validate(name, studentNo, college, phone, email) {
        if (!name.trim()) return '姓名不能为空';
        if (!studentNo.trim()) return '学号不能为空';
        if (!college) return '请选择学院';
        if (!/^1[3-9]\d{9}$/.test(phone)) return '手机号格式不正确';
        if (email && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) return '邮箱格式不正确';
        return null;
    }

    function submit() {
        const name = $('name').value.trim();
        const studentNo = $('studentNo').value.trim();
        const college = $('college').value;
        const phone = $('phone').value.trim();
        const email = $('email').value.trim();

        const err = validate(name, studentNo, college, phone, email);
        if (err) { $('error').textContent = err; return; }

        const a = currentActivity();
        if (currentSignups().some(s => s.studentNo === studentNo)) {
            $('error').textContent = '该学号已报名此活动，请勿重复报名';
            return;
        }
        if (currentSignups().length >= a.quota) {
            $('error').textContent = '该活动名额已满';
            return;
        }

        signups.push({
            id: String(Date.now() + Math.random()),
            activityId: a.id, name, studentNo, college, phone, email,
            time: new Date().toLocaleString()
        });
        save();
        $('error').textContent = '';
        $('name').value = $('studentNo').value = $('phone').value = $('email').value = '';
        $('college').value = '';
        $('modalText').textContent = `${name}（${studentNo}）已成功报名「${a.title}」`;
        $('successModal').style.display = 'flex';
        renderDetail(); renderList();
    }

    function cancelSignup(id) {
        if (!confirm('确认取消报名？')) return;
        signups = signups.filter(s => s.id !== id);
        save(); renderDetail(); renderList();
    }

    function closeModal() { $('successModal').style.display = 'none'; }

    function renderList() {
        const list = currentSignups();
        $('count').textContent = list.length;
        $('list').innerHTML = list.length
            ? list.map(s => `
                <li>
                    <span class="n">${escapeHtml(s.name)}</span>
                    <span class="m">${escapeHtml(s.studentNo)} · ${escapeHtml(s.college)}</span>
                    <button onclick="cancelSignup('${s.id}')">取消</button>
                </li>`).join('')
            : '<li class="empty">暂无报名，快来第一个报名吧</li>';
    }

    window.switchActivity = switchActivity;
    window.submit = submit;
    window.cancelSignup = cancelSignup;
    window.closeModal = closeModal;

    renderTabs(); renderDetail(); renderList();
})();