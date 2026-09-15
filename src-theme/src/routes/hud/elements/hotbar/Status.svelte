<script lang="ts">
    export let max: number;
    export let value: number;
    export let color: string;
    export let alignRight: boolean;
    export let label: string | null = null;
    export let icon: string | null = null;
</script>

<div class="progress" class:align-right={alignRight}>
    {#if label}
        <div class="label">{label}</div>
    {/if}
    {#if icon}
        <img class="icon" src="img/hud/hotbar/icon-{icon}.svg" alt={icon}>
    {/if}
    <div
            class:align-right={alignRight}
            class="progress-bar"
            style="width: {Math.floor(
            (value / max) * 100,
        )}%; background-color: {color};"
    ></div>
</div>

<style lang="scss">
  @use "../../../../colors.scss" as *;

  .label {
    color: $hotbar-text-color;
    position: absolute;
    font-size: 14px;
    right: 5px;
    top: 50%;
    transform: translateY(-50%);
  }

  .icon {
    position: absolute;
    left: 5px;
    top: 50%;
    transform: translateY(-50%);
  }

  .progress {
    position: relative;
    border: 1px solid $neon-dim;
    background-color: rgba($panel-color, 0.90);

    &.align-right {
      .label {
        left: 5px;
        right: unset;
      }

      .icon {
        right: 5px;
        left: unset;
      }
    }
  }

  .progress-bar {
    height: 20px;
    will-change: width;
    transition: ease width 0.2s;

    &.align-right {
      margin-left: auto;
    }
  }

  /* Esports：统一字体栈与等宽数字（补完注入） */
  .label {
    font-family: var(--font-display);
  }
  .label, .progress {
    font-variant-numeric: tabular-nums;
  }

  /* Esports-FONT-INJECT */
  .label {
    font-family: var(--font-display);
  }
  /* Esports-NUM-INJECT */
  .label, .progress {
    font-variant-numeric: tabular-nums;
  }
</style>
