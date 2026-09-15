<script lang="ts">
    import type { Component } from "svelte";

    type Tab = {
        title: string;
        content: Component;
    };

    let { tabs, activeTab = $bindable(0) } = $props<{
        tabs: Tab[];
        activeTab?: number;
    }>();

    const Active = $derived(tabs[activeTab]?.content);
</script>

<div class="tabs">
    <div class="available-tabs">
        {#each tabs as tab, index (tab.title)}
            <button
                    class="tab-button"
                    class:active={index === activeTab}
                    onclick={() => (activeTab = index)}
                    type="button"
            >
                {tab.title}
            </button>
        {/each}
    </div>

    <div class="content">
        {#if Active}
            {@render Active()}
        {/if}
    </div>
</div>

<style lang="scss">
  @use "../../../colors.scss" as *;

  /* Esports：0 圆角标签条；选中态用实心霓虹反白 */
  .available-tabs {
    position: fixed;
    top: 15px;
    left: 50%;
    transform: translateX(-50%);
    display: flex;
    gap: 4px;
    padding: 4px;
    background-color: rgba($panel-color, 0.92);
    border: 1px solid $neon-dim;
    box-shadow: 0 0 24px $neon-glow-sm;
    z-index: 9999999999;
  }

  .tab-button {
    background: transparent;
    color: $clickgui-text-dimmed-color;
    padding: 7px 18px;
    font-family: var(--font-display);
    font-size: 13px;
    font-weight: 700;
    letter-spacing: 1px;
    cursor: pointer;
    transition: background-color .16s ease, color .16s ease, border-color .16s ease;
    border: solid 1px transparent;

    &:hover {
      color: $clickgui-text-color;
      background-color: $neon-faint;
    }

    &.active {
      color: $void-color;
      background-color: $neon-color;
      border: 1px solid $neon-color;
      box-shadow: 0 0 16px $neon-glow-sm;
    }
  }

  /* Esports-FONT-INJECT */
  .tab-button {
    font-family: var(--font-display);
  }
</style>
