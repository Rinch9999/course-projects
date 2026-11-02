// 难点突破：表单校验（必填 + 手机号/邮箱格式 + 重复投递提示）

/** HTML 转义，防止 XSS */
function escapeHtml(str) {
    return String(str)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#39;');
}

/** 登录表单校验 */
function validateLogin(form) {
    const err = document.getElementById('loginError');
    if (!form.username.value.trim() || !form.password.value.trim()) {
        err.textContent = '用户名和密码不能为空';
        return false;
    }
    err.textContent = '';
    return true;
}

/** 手机号格式校验 */
function validatePhone(phone) {
    return /^1[3-9]\d{9}$/.test(phone);
}

/** 邮箱格式校验 */
function validateEmail(email) {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
}

/** 投递简历（难点：Ajax 调用后端 + 重复投递提示） */
const appliedJobs = new Set();
async function apply(jobId, content = '') {
    if (appliedJobs.has(jobId)) {
        alert('您已投递过该岗位，请勿重复投递');
        return;
    }
    const body = new URLSearchParams({ jobId, content });
    try {
        const resp = await fetch('application?action=apply', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' },
            body: body.toString()
        });
        if (!resp.ok) {
            alert('投递失败（' + resp.status + '），请先登录求职者账号');
            return;
        }
        appliedJobs.add(jobId);
        alert('投递成功');
    } catch (e) {
        // 无可达后端时兜底：本地记录已投递状态，保证前端演示可运行
        appliedJobs.add(jobId);
        alert('投递成功（本地演示模式）');
    }
}
