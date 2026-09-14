<script lang="ts">
    const idBase = `lokoami-logo-${Math.random().toString(36).slice(2, 9)}`;
    const petalGradientId = `${idBase}-petal`;
    const facetGradientId = `${idBase}-facet`;
</script>

<div class="animated-logo">
    <!-- Esports 规范：几何 / 有棱角，不使用柔和曲线。
         花瓣轮廓保留可辨识度，但改用多边形棱面 + 硬边描线。 -->
    <svg
            class="logo-svg"
            xmlns="http://www.w3.org/2000/svg"
            viewBox="0 0 100 140"
            role="img"
            aria-label="LokoAmi logo"
    >
        <defs>
            <linearGradient id={petalGradientId} x1="0" y1="0" x2="1" y2="1">
                <stop offset="0%" stop-color="#FF7FB0"/>
                <stop offset="100%" stop-color="#F0508C"/>
            </linearGradient>
            <linearGradient id={facetGradientId} x1="0" y1="0" x2="0" y2="1">
                <stop offset="0%" stop-color="#0A1628" stop-opacity="0.72"/>
                <stop offset="100%" stop-color="#0A1628" stop-opacity="0.34"/>
            </linearGradient>
        </defs>

        <g class="petal-part" transform="rotate(45 50 70)">
            <!-- 外轮廓：六边形棱面 -->
            <polygon points="50,2 76,46 94,96 50,148 6,96 24,46" fill={`url(#${petalGradientId})`}/>
            <!-- 内棱面 -->
            <polygon points="50,26 66,58 74,96 50,124 26,96 34,58" fill={`url(#${facetGradientId})`}/>
            <!-- 硬边分割线 -->
            <polyline points="50,2 50,148" stroke="#0A1628" stroke-width="2" opacity=".45" fill="none"/>
            <polyline points="24,46 76,46" stroke="#0A1628" stroke-width="2" opacity=".45" fill="none"/>
        </g>
    </svg>

    <div class="divider"></div>

    <div class="message">
        <div class="brand">Loko<span>Ami</span></div>
        <div class="credits">贡献清单 · 开发 <span class="who">lie_fish</span></div>
        <div class="credits">版本号 <span class="who">V0.1</span></div>
    </div>
</div>

<style>
    .animated-logo {
        display: flex;
        column-gap: 22px;
        align-items: center;
    }

    .logo-svg {
        display: block;
        width: 78px;
        height: 110px;
        flex: 0 0 auto;
        overflow: visible;
    }

    /* Esports：霓虹辉光，不用柔和投影 */
    .petal-part {
        filter: drop-shadow(0 0 16px rgba(240, 80, 140, 0.55));
    }

    /* 分隔条改为霓虹竖条，0 圆角 */
    .divider {
        background-color: #F0508C;
        width: 3px;
        flex: 0 0 auto;
        align-self: stretch;
        box-shadow: 0 0 14px rgba(240, 80, 140, 0.5);
    }

    .message {
        color: #FFFFFF;
        font-family: var(--font-body);
        font-size: 14px;
        font-weight: 400;
        line-height: 24px;
        justify-content: center;
        display: flex;
        flex-direction: column;
    }

    /* 品牌名用 Rajdhani（拉丁字形），字距按电竞规范拉开 */
    .brand {
        font-family: var(--font-display);
        font-size: 32px;
        font-weight: 700;
        letter-spacing: 3px;
        line-height: 38px;
        color: #FFFFFF;

        span {
            color: #F0508C;
            text-shadow: 0 0 18px rgba(240, 80, 140, 0.5);
        }
    }

    /* 中文署名回退系统字体，字距适度 */
    .credits {
        color: #94A3B8;
        font-size: 13px;
        letter-spacing: 1px;
    }

    .who {
        color: #F0508C;
        font-weight: 700;
    }
</style>
