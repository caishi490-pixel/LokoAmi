<script lang="ts">
    const idBase = `lokoami-logo-${Math.random().toString(36).slice(2, 9)}`;
    const petalGradientId = `${idBase}-petal`;
</script>

<div class="animated-logo">
    <svg
            class="logo-svg"
            xmlns="http://www.w3.org/2000/svg"
            viewBox="-34 -14 168 168"
            role="img"
            aria-label="LokoAmi logo"
    >
        <defs>
            <linearGradient id={petalGradientId} gradientUnits="userSpaceOnUse" x1="10" y1="0" x2="90" y2="140">
                <stop offset="0%" stop-color="#FFB3CE"/>
                <stop offset="45%" stop-color="#F0508C"/>
                <stop offset="100%" stop-color="#C92E68"/>
            </linearGradient>
        </defs>

        <!-- 樱花瓣：左倾 45°，无内部线条 -->
        <g class="petal-part" transform="rotate(45 50 70)">
            <path d="M50,6 C80,6 98,34 98,62 C98,96 74,124 56,134 L50,126 L44,134
                     C26,124 2,96 2,62 C2,34 20,6 50,6 Z" fill={`url(#${petalGradientId})`}/>
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
        column-gap: 26px;
        align-items: center;
    }

    .logo-svg {
        display: block;
        width: 96px;
        height: 96px;
        flex: 0 0 auto;
        overflow: visible;
    }

    .petal-part {
        filter: drop-shadow(0 4px 16px rgba(240, 80, 140, 0.55));
    }

    .divider {
        background-color: rgba(240, 80, 140, 0.45);
        width: 2px;
        flex: 1;
        align-self: stretch;
    }

    .message {
        color: #EAF2FF;
        font-size: 15px;
        font-weight: 500;
        line-height: 26px;
        justify-content: center;
        display: flex;
        flex-direction: column;
    }

    .brand {
        font-size: 32px;
        font-weight: 800;
        letter-spacing: 0.4px;
        line-height: 40px;

        span {
            color: #F0508C;
        }
    }

    .credits {
        color: #8FA6C4;
        font-size: 13px;
        letter-spacing: 0.6px;
    }

    .who {
        color: #F0508C;
        font-weight: 700;
    }
</style>