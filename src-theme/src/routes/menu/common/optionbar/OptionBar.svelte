<script lang="ts">
    import {fly} from "svelte/transition";
</script>

<div class="option-bar" transition:fly|global={{duration: 700, x: 1000}}>
    <slot />
</div>

<style lang="scss">
    @use "../../../../colors.scss" as *;

    /* Esports：0 圆角 + 右上切角 + 霓虹描边 */
    .option-bar {
      background-color: rgba($panel-color, 0.68);
      border: 1px solid $neon-dim;
      clip-path: polygon(0 0, calc(100% - 10px) 0, 100% 10px, 100% 100%, 0 100%);
      padding: 15px 30px;
      display: flex;
      align-items: center;
      column-gap: 30px;
      margin-bottom: 25px;
    }
</style>