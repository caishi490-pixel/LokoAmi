<script lang="ts">
    import {createEventDispatcher} from "svelte";

    let value = "";

    const dispatch = createEventDispatcher<{
        search: { query: string }
    }>();
</script>

<input class="search" type="text" placeholder="搜索…" bind:value={value}
       on:input={() => dispatch("search", {query: value})} spellcheck="false">

<style lang="scss">
  @use "../../../colors.scss" as *;

  /* Esports：0 圆角；下缘霓虹描边兼作强调线 */
  .search {
    background-color: rgba($panel-color, .36);
    border: none;
    color: $menu-text-color;
    font-family: var(--font-display);
    font-size: 20px;
    font-weight: 600;
    letter-spacing: 1px;
    border-bottom: solid 2px $neon-color;
    box-shadow: 0 6px 18px -10px $neon-glow;
    background-image: url("/img/menu/icon-search.svg");
    background-repeat: no-repeat;
    background-position: 20px center;
    padding: 20px 20px 20px 60px;
    flex: 1;

    &::placeholder {
      color: $menu-text-dimmed-color;
    }
  }
</style>