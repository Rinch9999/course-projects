// 难点突破：ECharts 图表（岗位热度 / 企业活跃度）

// 示例数据（后端接口不可用时兜底，保证图表可渲染）
const SAMPLE_HOT = [
    { title: 'Java 后端开发工程师', applyCount: 42 },
    { title: '前端开发工程师', applyCount: 35 },
    { title: '产品经理', applyCount: 30 },
    { title: '数据分析师', applyCount: 28 },
    { title: '测试工程师', applyCount: 21 },
    { title: '运营专员', applyCount: 18 }
];

const SAMPLE_ACTIVE = [
    { name: '星辰科技', value: 12 },
    { name: '云帆网络', value: 9 },
    { name: '数智咨询', value: 7 },
    { name: '创新工场', value: 5 },
    { name: '青柠传媒', value: 4 }
];

/** 请求后端 JSON，失败回退示例数据 */
async function fetchJSON(url, fallback) {
    try {
        const resp = await fetch(url);
        if (!resp.ok) return fallback;
        const data = await resp.json();
        const list = Array.isArray(data) ? data : (data && data.list);
        return list && list.length ? list : fallback;
    } catch (e) {
        return fallback;
    }
}

/** 渲染岗位热度柱状图（难点） */
async function renderHotChart() {
    const data = await fetchJSON('job?action=hot', SAMPLE_HOT);
    const chart = echarts.init(document.getElementById('hotChart'));
    chart.setOption({
        title: { text: '岗位热度排行', left: 'center' },
        tooltip: {},
        xAxis: { type: 'category', data: data.map(d => d.title) },
        yAxis: { type: 'value', name: '投递量' },
        series: [{
            type: 'bar',
            data: data.map(d => d.applyCount),
            itemStyle: { color: '#1f3b5c' }
        }]
    });
}

/** 渲染企业活跃度饼图（难点） */
function renderActivePie(containerId, data) {
    const chart = echarts.init(document.getElementById(containerId));
    chart.setOption({
        title: { text: '企业活跃度（发布岗位数）', left: 'center' },
        tooltip: { trigger: 'item' },
        legend: { bottom: 0 },
        series: [{
            type: 'pie',
            radius: '55%',
            data: (data && data.length) ? data : SAMPLE_ACTIVE
        }]
    });
}