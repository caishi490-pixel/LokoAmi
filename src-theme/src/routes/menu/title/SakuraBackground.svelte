<script lang="ts">
    /*
     * 启动页背景 —— 设计稿 v3
     *
     * 层次：底色 → 双色径向辉光 → 点阵（径向遮罩）→ 飘落樱瓣 → 右侧巨型水印
     *
     * ⚠ 关于底色：设计稿的背景是**不透明**的，因此会盖住游戏画面与「切换光影」的
     *   着色器背景。若要恢复透出效果，把 .sakura-bg 的 --title-bg 改成带透明度的值即可，
     *   例如 rgba(10, 22, 40, .72)。
     */

    const petalPath =
        "M50,6 C80,6 98,34 98,62 C98,96 74,124 56,134 L50,126 L44,134 " +
        "C26,124 2,96 2,62 C2,34 20,6 50,6 Z";

    type Petal = {
        id: number;
        left: string;
        width: number;
        height: number;
        duration: number;
        delay: number;
        color: string;
    };

    /* 与设计稿一致：四片，颜色在玫粉 / 浅粉 / 天蓝之间错开 */
    const petals: Petal[] = [
        {id: 0, left: "12%", width: 22, height: 31, duration: 17, delay: 0, color: "#F0508C"},
        {id: 1, left: "34%", width: 16, height: 22, duration: 23, delay: -6, color: "#FF8FB8"},
        {id: 2, left: "68%", width: 19, height: 27, duration: 20, delay: -12, color: "#38BDF8"},
        {id: 3, left: "87%", width: 24, height: 34, duration: 26, delay: -3, color: "#F0508C"}
    ];
</script>

<div class="sakura-bg" aria-hidden="true">
    <div class="glow-rose"></div>
    <div class="glow-sky"></div>
    <div class="dots"></div>

    <!-- 右侧巨型水印：同样左倾 45° -->
    <svg class="watermark" viewBox="0 0 100 140" xmlns="http://www.w3.org/2000/svg">
        <path d={petalPath} fill="#F0508C"/>
    </svg>

    <div class="petals">
        {#each petals as petal (petal.id)}
            <svg
                    class="petal"
                    style="left:{petal.left};width:{petal.width}px;height:{petal.height}px;animation-duration:{petal.duration}s;animation-delay:{petal.delay}s"
                    viewBox="0 0 100 140"
                    xmlns="http://www.w3.org/2000/svg"
            >
                <path d={petalPath} fill={petal.color}/>
            </svg>
        {/each}
    </div>
</div>

<style>
    .sakura-bg {
        /* 想透出游戏画面 / 光影背景，把这里换成 rgba(10, 22, 40, .72) 之类即可 */
        --title-bg: #0A1628;

        position: fixed;
        inset: 0;
        z-index: -1;
        overflow: hidden;
        pointer-events: none;
        background-color: var(--title-bg);
        contain: layout style paint;
    }

    /* 右上玫粉辉光 */
    .glow-rose {
        position: absolute;
        width: 1200px;
        height: 1200px;
        border-radius: 50%;
        right: -380px;
        top: -320px;
        background: radial-gradient(circle,
        rgba(240, 80, 140, .17) 0%,
        rgba(240, 80, 140, .05) 42%,
        transparent 70%);
    }

    /* 左下天蓝辉光 */
    .glow-sky {
        position: absolute;
        width: 900px;
        height: 900px;
        border-radius: 50%;
        left: -300px;
        bottom: -360px;
        background: radial-gradient(circle,
        rgba(56, 189, 248, .15) 0%,
        transparent 70%);
    }

    /* 点阵：用径向遮罩让边缘自然消失 */
    .dots {
        position: absolute;
        inset: 0;
        background-image: radial-gradient(rgba(143, 166, 196, .16) 1px, transparent 1px);
        background-size: 26px 26px;
        mask-image: radial-gradient(ellipse 78% 68% at 34% 46%, #000 18%, transparent 74%);
        -webkit-mask-image: radial-gradient(ellipse 78% 68% at 34% 46%, #000 18%, transparent 74%);
    }

    .watermark {
        position: absolute;
        right: 20px;
        top: 50%;
        width: 460px;
        height: 460px;
        opacity: .055;
        transform: translateY(-50%) rotate(45deg);
    }

    .petals {
        position: absolute;
        inset: 0;
    }

    .petal {
        position: absolute;
        top: 0;
        opacity: .13;
        animation-name: petal-fall;
        animation-timing-function: linear;
        animation-iteration-count: infinite;
        will-change: transform;
    }

    @keyframes petal-fall {
        0% {
            transform: translateY(-14vh) rotate(45deg);
        }

        100% {
            transform: translateY(114vh) rotate(365deg);
        }
    }
</style>
