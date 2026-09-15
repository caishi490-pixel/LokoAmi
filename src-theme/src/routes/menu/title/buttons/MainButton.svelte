<script lang="ts">
    import {fade, fly} from "svelte/transition";
    import {createEventDispatcher} from "svelte";
    import {backIn, backOut} from "svelte/easing";

    export let title: string;
    /* 保留 icon 入参以兼容调用方；设计稿 v3 的按钮用编号代替图标 */
    export let icon: string;
    export let index: number;
    /* 主按钮：玫粉渐变实心（设计稿里 LokoAmi 那一项） */
    export let primary = false;

    let hovered = false;

    const dispatch = createEventDispatcher();

    $: indexLabel = String(index + 1).padStart(2, "0");
</script>

<!-- svelte-ignore a11y-no-static-element-interactions -->
<!-- svelte-ignore a11y-click-events-have-key-events -->
<div class="main-button" class:primary on:mouseenter={() => hovered = true} on:mouseleave={() => hovered = false}
     on:click={() => hovered = false}
     on:click={() => dispatch("click")} out:fly|global={{duration: 400, x: -500, delay: index * 100, easing: backIn}}
     in:fly|global={{duration: 400, x: -500, delay: index * 100, easing: backOut}}>
    <span class="n">{indexLabel}</span>

    <div class="title">{title}</div>

    <div class="wrapped-content">
        <slot parentHovered={hovered}/>
    </div>

    <span class="ar">→</span>
</div>

<style lang="scss">
  /* 启动页设计稿 v3：圆角面板 + 编号 + 悬停右移与箭头。
     注意这里刻意不使用全站的 0 圆角 / 切角规范 —— 启动页以 v3 设计稿为准。 */
  .main-button {
    position: relative;
    display: flex;
    align-items: center;
    gap: 15px;
    min-width: 340px;
    padding: 15px 24px;
    overflow: hidden;
    cursor: pointer;
    text-align: left;

    background-color: rgba(18, 34, 54, .82);
    border: 1px solid rgba(143, 166, 196, .14);
    border-radius: 9px;

    transition: background-color .2s cubic-bezier(.4, 0, .2, 1),
    border-color .2s cubic-bezier(.4, 0, .2, 1),
    box-shadow .2s cubic-bezier(.4, 0, .2, 1),
    transform .2s cubic-bezier(.4, 0, .2, 1);
    will-change: transform;
  }

  .n {
    min-width: 19px;
    font-family: var(--font-display);
    font-size: 11px;
    font-weight: 700;
    letter-spacing: 1px;
    color: #8FA6C4;
    transition: color .2s;
  }

  /* 中文会回退到系统字体（Rajdhani 无中文字形） */
  .title {
    font-family: var(--font-display);
    font-size: 15px;
    font-weight: 600;
    letter-spacing: 1.5px;
    color: #EAF2FF;
  }

  .wrapped-content {
    display: contents;
  }

  .ar {
    margin-left: auto;
    padding-left: 14px;
    font-size: 15px;
    color: #F0508C;
    opacity: 0;
    transform: translateX(-7px);
    transition: opacity .2s, transform .2s;
  }

  .main-button:hover {
    background-color: #182C46;
    border-color: rgba(240, 80, 140, .42);
    box-shadow: 0 8px 26px rgba(240, 80, 140, .16);
    transform: translateX(4px);

    .n {
      color: #F0508C;
    }

    .ar {
      opacity: 1;
      transform: translateX(0);
    }
  }

  /* 主按钮：玫粉渐变实心 */
  .main-button.primary {
    background: linear-gradient(96deg, #F0508C 0%, #FF6FA5 100%);
    border-color: transparent;
    box-shadow: 0 10px 30px rgba(240, 80, 140, .36);

    .title {
      color: #12030A;
      font-weight: 800;
    }

    .n {
      color: rgba(18, 3, 10, .5);
    }

    .ar {
      color: #12030A;
    }
  }

  .main-button.primary:hover {
    box-shadow: 0 14px 40px rgba(240, 80, 140, .55);
    transform: translateX(4px) scale(1.008);
  }
</style>
