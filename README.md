<div align="center">

<img width="168" src="src-theme/public/img/lb-logo.svg" alt="LokoAmi">

# LokoAmi

**基于 LiquidBounce 二次开发的 Minecraft Fabric 客户端**
**拆掉全部对抗性模块 · 只留看得见、用得上的功能**

[![Minecraft](https://img.shields.io/badge/Minecraft-1.21.11-3C8527?style=flat-square)](https://minecraft.net)
[![Loader](https://img.shields.io/badge/Fabric%20Loader-%E2%89%A5%200.16.14-DBD0B4?style=flat-square)](https://fabricmc.net)
[![License](https://img.shields.io/badge/License-GPL--3.0-F0508C?style=flat-square)](LICENSE)

作者 **lie_fish**（管弦彩石） · 反馈 **caishi490@gmail.com**

</div>

---

## 这是什么

LokoAmi 由 **LiquidBounce 0.37.0** 二次开发而来。

上游是一个功能庞杂的 PVP 对抗客户端。LokoAmi 的方向是做**减法**：把战斗、反检测、服务端漏洞利用一类的对抗性模块**整类拆除**，只留下渲染显示与日常操作辅助，再把整套界面重做一遍。

目标不是"更弱的作弊端"，而是一个**可以安心在日常游玩与角色扮演服务器上开着的客户端** —— 不打架、不碰服务端、不需要担心被别人发现。

标识是一枚**左倾 45° 的樱花瓣**，配色为**深海蓝黑 + 玫粉**。

## 当前版本：v0.15

**视觉重构完成 · 界面中文化 · 照明修复**

| 项目 | 内容 |
|---|---|
| 模块规模 | 上游约 **234 个** → **49 个**（Combat / Player / Fun 三个分类已清空） |
| 混入点 | `lokoami.mixins.json` 客户端混入 **136 项** |
| 界面 | **121 个** Svelte 组件全量重做；中英语言文件 **781 / 777** 条 |
| 配色 | 深海玫瑰 —— `#0A1628` 深海蓝黑 / `#F0508C` 玫粉 |
| 命名空间 | 模组 ID、资源包、语言文件统一迁移至 `lokoami` |
| 修复 | FullBright 伽马模式在光照贴图缓存环境下完全失效（见下方工程笔记） |

## 模块一览

共 **49** 个模块。

### Render（28）—— 视觉与相机

`Animations` `Aspect` `AutoF5` `BetterInventory` `BlockOutline` `Breadcrumbs` `ClickGUI`
`CombineMobs` `Crosshair` `CustomAmbience` `DamageParticles` `Debug` `FreeCam` `FreeLook`
`FullBright` `HUD` `Hats` `HitFX` `ItemTags` `NoBob` `NoFOV` `NoHurtCam` `NoSwing`
`Particles` `QuickPerspectiveSwap` `SkinChanger` `SmoothCamera` `Zoom`

### Misc（10）—— 界面与信息

`BetterChat` `BetterTab` `BetterTitle` `DebugRecorder` `GUICloser` `ItemScroller`
`MiddleClickAction` `Notifier` `PacketLogger` `ReportHelper`

### Exploit（3）—— 已收敛为兼容性修复

`Plugins` `PortalMenu` `YggdrasilSignatureFix`

### Custom（6）—— 为角色扮演服务器预留

`AdminActionLog` `ArcaneRange` `EntitySizeNormalize` `FishGuard`
`PacketCapture` `SimmcChannel`

> ⚠️ 这 6 个模块目前是**空壳占位**（各约 30 行），仅有骨架与配置项，尚无实际功能。

### Movement（1） / World（1）

`Sprint` / `AutoDisable`

### 已清空的分类

`Combat`、`Player`、`Fun` —— 分类本身保留，模块已全部移除。

## 被移除的部分

这是本分支与上游最大的区别：共移除约 **185** 个模块类文件（模块数 **234 → 49**）。被整体拆除的包括：

- **战斗类**：KillAura、Aimbot、AutoBow、Criticals、CrystalAura、Velocity、Backtrack、TickBase、Hitbox、AutoArmor、TpAura、ElytraTarget 等
- **玩家类**：NoFall、Blink、ChestStealer、InventoryCleaner、Offhand、AutoFish、Reach、Phase 等
- **漏洞利用类**：Disabler（含十余种针对具体反作弊的实现）、Dupe、**ServerCrasher**、Clip、ClickTp、PingSpoof、TimeShift、ResetVL 等
- **世界类**：Nuker、Scaffold、AutoFarm、Fucker、AutoBuild、Surround、HoleFiller、Timer 等
- **移动类**：Fly（含全部服务器专用模式）、Speed、LongJump、ElytraFly、AutoDodge 等
- **渲染对抗类**：**XRay**、ESP、Chams、Tracers、Nametags、StorageESP、VoidESP 等
- **杂项类**：AntiBot、AntiStaff、Spammer、NameProtect、Teams 等

> 移除后 `Combat` / `Player` / `Fun` 分类为空，相关命令与配置项已同步清理，不留悬空引用。

## 视觉与交互

界面（`src-theme/`，Svelte 5 + SCSS + Vite）按**电竞风格**重做：

| 角色 | 色值 |
|---|---|
| 主背景（深海） | `#0A1628` |
| 卡片面 | `#0E1F3A` |
| 抬升面 / 轨道 | `#14294A` |
| 霓虹主色（玫粉） | `#F0508C` |
| 危险 / 警告 / 正常 | `#FF2D55` / `#F5A623` / `#39FF14` |
| 主文字 / 次级文字 | `#FFFFFF` / `#94A3B8` |

设计语言：**0 圆角**、切角容器、霓虹辉光描边、Rajdhani 显示字体（中文回退至 Noto Sans SC / 微软雅黑 / 苹方）、数值统一 `tabular-nums`。

HUD、ClickGUI、启动页、设置面板均已完成结构级改造，不再是单纯换色。界面文本已中文化。

## 安装

1. 安装 **Fabric Loader**（≥ `0.16.14`）
2. 游戏版本需为 **Minecraft 1.21.11**
3. 从 [Releases](https://github.com/caishi490-pixel/LokoAmi/releases) 下载 `LokoAmi-0.15.jar`
4. 放入 `.minecraft/mods/`
5. 确认两个前置模组同样位于 `mods/`：
   - [Fabric API](https://modrinth.com/mod/fabric-api) —— 构建所依赖版本 `0.140.0+1.21.11`
   - [Fabric Language Kotlin](https://modrinth.com/mod/fabric-language-kotlin) —— `1.13.9+kotlin.2.3.10`
6. 启动游戏

配置目录为 **`LokoAmi/`**（由客户端常量 `CLIENT_NAME` 决定）。若你的实例里还留着旧版遗留的 `LiquidBounce/` 目录，当前版本不读取它。

> 建议不要同时把两个 LokoAmi jar 放进 `mods/`，Fabric 会以 `Duplicate mod` 拒绝启动。

## 从源码构建

需要 **JDK 21** 与 **Node.js**（后者用于构建界面主题）。

```bash
git clone https://github.com/caishi490-pixel/LokoAmi.git
cd LokoAmi
./gradlew assemble
```

产物位于 `build/libs/LokoAmi-0.15.jar`。版本号在 `gradle.properties` 的 `mod_version` 中调整，产物文件名跟随 `archives_base_name`。

开发时可用 `./gradlew runClient` 直接启动，`./gradlew genSources` 生成可读的 Minecraft 源码（可选，便于调试）。

## 已知问题

- **6 个 Custom 模块是空壳**，仅有骨架，暂无实际功能
- 部分 HUD 元素在极端分辨率下间距需手动微调
- v0.15 尚未经过大规模实机验证

## 工程笔记（改这个项目时踩过的坑）

### 1. 光照贴图缓存会让「拦截返回值」型的注入彻底失效

`FullBright` 的伽马模式不修改真实的 `options.gamma()`，而是在 `LightTexture.updateLightTexture` 内部拦截 `gamma().get()` 的返回值。这带来一个隐蔽后果：

`updateLightTexture` 的第一行是 `if (!this.updateLightTexture) return;`，而这个脏标记**只由 `LightTexture.tick()` 置位**。于是任何光照贴图缓存模组（例如 BadOptimizations 的 `enable_lightmap_caching`）只要比对真实 gamma 值后认定"无变化"，就会取消 `tick()`，脏标记永不置位，注入永远不执行 —— **且不产生任何报错**。

修法：在 `updateLightTexture` 读取该脏标记处按自身伽马值强制刷新，并在关闭时补一次刷新以恢复画面。不依赖任何第三方模组的内部实现，也不修改用户配置。

### 2. SCSS 变量是编译期的，传不给 Svelte 内联样式

```svelte
<!-- ✗ 静默失效：$danger-color 不是合法 CSS，整条声明被浏览器丢弃 -->
<div style="background-color: {$dangerColor}">

<!-- ✓ 用运行时 CSS 自定义属性 -->
<div style="background-color: var(--danger)">
```

本项目在 `app.scss` 中定义了 `--neon / --void / --panel / --ok / --warn / --danger / --text / --text-2` 一组运行时变量供内联样式使用。曾经因为把 SCSS 变量写进内联 `style=`，导致血条、饱食度、经验条整体渲染成黑条，且没有任何错误提示。

### 3. Mixin 的 `ordinal` 只在目标方法内部计数

`@At(..., ordinal = N)` 统计的是**目标方法内**第 N 个匹配指令，不是整个类的顺序。跨方法数 ordinal 会瞄准完全无关的调用点。判断目标时必须把字节码按方法切段后再数。

另：Kotlin 的 `internal` 成员在 JVM 上带编译期模块名后缀，后缀跟随 Gradle `rootProject.name`。本项目 `rootProject.name = "LokoAmi"`，故后缀为 `$LokoAmi`。若改动 `settings.gradle.kts` 中的 `rootProject.name`，必须同步修改 `MixinPacketWrapper.java` 中对 `InventoryManager.setInventoryOpenServerSide$LokoAmi(...)` 的调用 —— 全项目仅此 1 处 Java 侧引用该混淆名，漏改会直接导致 `:compileJava` 报「找不到符号」。

## 致谢

本项目建立在 **LiquidBounce** 之上，感谢 **CCBlueX** 及全体上游贡献者。

- 上游项目：<https://github.com/CCBlueX/LiquidBounce>
- 上游官网：<https://liquidbounce.net>

同时感谢 **Fabric** 与 **Fabric Language Kotlin** 团队。

---

## License

本项目遵循 **[GNU General Public License v3.0](LICENSE)**。

> 本仓库是 LiquidBounce 的衍生作品，**整体必须以 GPL-3.0 授权**，不得改用其他许可证（例如 The Unlicense、MIT）。

本许可仅适用于直接位于本仓库中的源代码。在开发与编译过程中可能使用了我们未持有权利的其他源代码，此类代码不受 GPL 许可覆盖。

若你不熟悉该许可证，以下是其主要条款的简要说明（**不构成法律建议，亦不具法律约束力**）：

*你可以做的事：*

- 使用
- 分享
- 修改

*如果你确实使用了本项目中的任何代码：*

- **你必须公开你的修改作品的源代码，以及你从本项目取用的源代码。这意味着你不得将本项目代码（哪怕是部分）用于闭源（或混淆）的应用程序。**
- **你的修改作品同样必须以 GPL-3.0 授权。**
- **你必须保留原作者的版权声明与本许可证文本。**
- **本作品不提供任何担保。**

---

<div align="center">

*Made with 🐋 & 🌸 by lie_fish*

**本项目为非官方作品，与 Mojang Studios、Microsoft 及 CCBlueX 均无隶属或合作关系。**

</div>
