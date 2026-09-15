<script lang="ts">
    import {type ComponentType, createEventDispatcher} from "svelte";

    let availableTabsElement: HTMLElement | undefined;

    export let tabs: {
        title: string,
        icon: string,
        component: ComponentType,
    }[];
    export let activeTab = 0;

    const dispatch = createEventDispatcher<{
        changeTab: { activeTab: number }
    }>();

    function setActiveTab(i: number) {
        activeTab = i;
        dispatch("changeTab", {activeTab});
    }
</script>

<div class="tabs">
    <div class="available-tabs" bind:this={availableTabsElement}>
        {#each tabs as {title, icon}, index}
            <button class="tab-button" class:active={tabs[activeTab].title === title}
                    on:click={() => setActiveTab(index)}>
                <img class="icon" src="img/menu/altmanager/{icon}" alt={title}>
                <span>{title}</span>
            </button>
        {/each}
    </div>

    <div style="width: {availableTabsElement?.clientWidth}px">
        <svelte:component this={tabs[activeTab].component}/>
    </div>
</div>

<style lang="scss">
  @use "../../../../colors.scss" as *;

  .available-tabs {
    display: flex;
    column-gap: 10px;
    margin-bottom: 40px;
  }

  /* Esports：0 圆角；选中态用霓虹描边 + 淡霓虹底 + 辉光 */
  .tab-button {
    font-family: var(--font-display);
    font-weight: 700;
    letter-spacing: 1px;
    background-color: rgba($panel-color, .36);
    color: $menu-text-color;
    padding: 10px;
    border: solid 2px transparent;
    flex-grow: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
    row-gap: 10px;
    cursor: pointer;
    transition: ease border-color .2s, ease background-color .2s, ease box-shadow .2s;

    .icon {
      height: 30px;
    }

    &.active {
      border-color: $neon-color;
      background-color: $neon-faint;
      box-shadow: 0 0 20px $neon-glow-sm;
    }
  }

  /* Esports-FONT-INJECT */
  .tab-button {
    font-family: var(--font-display);
  }
</style>