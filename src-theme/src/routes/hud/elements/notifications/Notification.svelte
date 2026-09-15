<script lang="ts">
    export let title: string;
    export let message: string;
    export let severity: string;
</script>

<div class="notification">
    <div class="icon {severity.toString().toLowerCase()}"></div>
    <div class="title">{title}</div>
    <div class="message">{message}</div>
</div>

<style lang="scss">
  
  @use "../../../../colors.scss" as *;
/* Esports：0 圆角 + 右上切角 + 霓虹描边与辉光 */
  .notification {
    display: grid;
    grid-template-areas:
            "a b"
            "a c";
    grid-template-columns: max-content 1fr;
    column-gap: 10px;
    background: rgba($panel-color, 0.94);
    border: 1px solid $neon-dim;
    clip-path: polygon(0 0, calc(100% - 12px) 0, 100% 12px, 100% 100%, 0 100%);
    box-shadow: 0 0 22px $neon-glow-sm;
    width: 300px;
    overflow: hidden;
    padding: 10px;
    margin-bottom: 10px;
  }

  .icon {
    height: 40px;
    width: 40px;
    background-position: center;
    background-repeat: no-repeat;
    border-radius: 0;
    grid-area: a;
    transition: background-color 0.2s;
    position: relative;
    background-image: url("/img/hud/notification/icon-toggle.svg");

    &.success {
      background-color: $ok-color;
      background-image: url("/img/hud/notification/icon-success.svg");
    }

    &.error {
      background-color: $danger-color;
      background-image: url("/img/hud/notification/icon-error.svg");
    }

    &.info {
      background-color: $neon-color;
      background-image: url("/img/hud/notification/icon-info.svg");
    }

    &.disabled,
    &.enabled {
      &::after {
        content: "";
        position: absolute;
        height: 10px;
        width: 10px;
        border-radius: 0;
        top: 50%;
        transform: translate(-50%, -50%);
        background: white;
        transition: all 0.2s ease-out;
      }
    }

    &.enabled {
      background-color: $ok-color;

      &::after {
        left: 62%;
      }
    }

    &.disabled {
      background-color: $danger-color;

      &::after {
        left: 38%;
      }
    }
  }

  .title {
    grid-area: b;
    font-family: var(--font-display);
    font-size: 14px;
    color: white;
    font-weight: 700;
    letter-spacing: 1px;
  }

  .message {
    grid-area: c;
    font-size: 12px;
    color: #94A3B8;
  }

  /* Esports-FONT-INJECT */
  .title, .message {
    font-family: var(--font-display);
  }
</style>
