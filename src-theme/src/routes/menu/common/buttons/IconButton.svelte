<script lang="ts">
    import ToolTip from "../ToolTip.svelte";
    import {createEventDispatcher} from "svelte";

    export let title: string;
    export let icon: string;
    /*
     * appearance:
     *   "default" —— 全站现有外观（无底色的 58×58 图标位）
     *   "v3"      —— 启动页设计稿 v3 的方形社交按钮
     */
    export let appearance: "default" | "v3" = "default";

    const dispatch = createEventDispatcher();
</script>

<!-- svelte-ignore a11y-click-events-have-key-events -->
<!-- svelte-ignore a11y-no-static-element-interactions -->
<div class="icon-button" class:v3={appearance === "v3"} on:click={() => dispatch("click")}>
    <ToolTip text={title}/>

    <div class="icon">
        <img src="img/menu/icon-{icon}.svg" alt={icon}>
    </div>
</div>

<style lang="scss">
  .icon-button {
    position: relative;
  }

  .icon {
    height: 58px;
    width: 58px;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
  }

  /* ---------- 设计稿 v3 ---------- */
  .icon-button.v3 {
    .icon {
      width: 38px;
      height: 38px;
      border-radius: 8px;
      background-color: rgba(18, 34, 54, .82);
      border: 1px solid rgba(143, 166, 196, .14);
      transition: background-color .2s, border-color .2s, box-shadow .2s;
    }

    .icon img {
      width: 16px;
      height: 16px;
      opacity: .78;
      transition: opacity .2s;
    }

    .icon:hover {
      background-color: #1A1F38;
      border-color: rgba(240, 80, 140, .5);
      box-shadow: 0 6px 20px rgba(240, 80, 140, .2);

      img {
        opacity: 1;
      }
    }
  }
</style>
