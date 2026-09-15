<script lang="ts">
    import RippleLoader from "../../RippleLoader.svelte";

    export let url: string;

    let previewImageLoaded = false;
</script>

<div class="avatar-wrapper">
    {#if !previewImageLoaded}
        <div class="loader">
            <RippleLoader />
        </div>
    {/if}

    <img class="avatar" on:load={() => previewImageLoaded = true} src={url} alt="avatar">
</div>

<style lang="scss">
  @use "../../../../../colors.scss" as *;

  .avatar-wrapper {
    grid-area: a;
    height: 50px;
    width: 50px;
    position: relative;
  }

  /* Esports：头像由圆形改为切角方块 */
  .avatar {
    height: 100%;
    width: 100%;
    border: 1px solid $neon-dim;
    clip-path: polygon(0 0, calc(100% - 10px) 0, 100% 10px, 100% 100%, 0 100%);
    image-rendering: pixelated;
  }

  .loader {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
  }
</style>