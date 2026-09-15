<script lang="ts">
    /*
     * 启动页品牌区 —— 设计稿 v3
     *
     * 左倾 45° 樱瓣 + 品牌名（Ami 用玫粉）+ 版本徽章。
     * 版本号来自 getClientInfo()，不再写死；取不到时只隐藏徽章。
     * 贡献清单（开发 / 版本号）按设计稿移到主区卡片里，不在顶栏。
     */
    import {onMount} from "svelte";
    import {getClientInfo} from "../../../../integration/rest";

    const idBase = `lokoami-logo-${Math.random().toString(36).slice(2, 9)}`;
    const petalGradientId = `${idBase}-petal`;

    let version = "";

    onMount(async () => {
        try {
            const info = await getClientInfo();
            version = info.clientVersion;
        } catch {
            version = "";
        }
    });
</script>

<div class="brand">
    <div class="logo-box">
        <svg
                class="logo"
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 100 140"
                role="img"
                aria-label="LokoAmi logo"
        >
            <defs>
                <linearGradient id={petalGradientId} x1="0" y1="0" x2="0.4" y2="1">
                    <stop offset="0%" stop-color="#FFB3CE"/>
                    <stop offset="45%" stop-color="#F0508C"/>
                    <stop offset="100%" stop-color="#C92E68"/>
                </linearGradient>
            </defs>
            <path
                    d="M50,6 C80,6 98,34 98,62 C98,96 74,124 56,134 L50,126 L44,134
                       C26,124 2,96 2,62 C2,34 20,6 50,6 Z"
                    fill={`url(#${petalGradientId})`}
            />
        </svg>
    </div>

    <span class="nm">Loko<i>Ami</i></span>

    {#if version}
        <span class="ver">V{version}</span>
    {/if}
</div>

<style>
    .brand {
        display: flex;
        align-items: center;
        gap: 15px;
    }

    /* 52×52 的定位盒，让旋转后的花瓣视觉居中 */
    .logo-box {
        width: 52px;
        height: 52px;
        display: grid;
        place-items: center;
        flex: 0 0 auto;
    }

    /* ★ 左倾 45°，无内部线条 */
    .logo {
        display: block;
        width: 32px;
        height: 45px;
        transform: rotate(45deg);
        filter: drop-shadow(0 4px 16px rgba(240, 80, 140, .55));
        overflow: visible;
    }

    .nm {
        font-family: var(--font-display);
        font-size: 25px;
        font-weight: 800;
        letter-spacing: .4px;
        color: #EAF2FF;
        white-space: nowrap;
    }

    .nm i {
        font-style: normal;
        color: #F0508C;
    }

    .ver {
        font-family: var(--font-display);
        font-size: 10px;
        font-weight: 700;
        letter-spacing: 1.4px;
        color: #38BDF8;
        border: 1px solid rgba(56, 189, 248, .34);
        border-radius: 5px;
        padding: 3px 7px;
        white-space: nowrap;
    }
</style>
