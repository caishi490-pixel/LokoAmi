<script lang="ts">
    /*
     * appearance:
     *   "default" —— 全站现有外观（面板底色 + 内边距），其他菜单页在用
     *   "v3"      —— 启动页设计稿 v3 的底栏：无容器，仅一排按钮
     */
    export let appearance: "default" | "v3" = "default";
</script>

<div class="button-container" class:v3={appearance === "v3"}>
    <slot />
</div>

<style lang="scss">
    @use "../../../../colors.scss" as *;

    .button-container {
      background-color: rgba($panel-color, 0.68);
      padding: 15px 30px;
      width: max-content;
      border-radius: 0;
      display: flex;
      column-gap: 20px;
    }

    /* 设计稿 v3：底栏按钮各自成块，不再套一层面板 */
    .button-container.v3 {
      background-color: transparent;
      padding: 0;
      column-gap: 9px;
      align-items: center;
    }
</style>
