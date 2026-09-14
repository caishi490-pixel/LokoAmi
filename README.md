<div align="center">

<img width="168" src="src-theme/public/img/lb-logo.svg" alt="LokoAmi">

# LokoAmi

**基于 LiquidBounce 二次开发的 Minecraft Fabric 客户端**
**删除高风险的对抗类模块 · 重做视觉与交互**

[![Minecraft](https://img.shields.io/badge/Minecraft-1.21.11-3C8527?style=flat-square)](https://minecraft.net)
[![License](https://img.shields.io/badge/License-GPL--3.0-F0508C?style=flat-square)](LICENSE)

作者 **lie_fish**（管弦彩石） · 反馈 **caishi490@gmail.com**

</div>

---

## 这是什么

LokoAmi 由 **LiquidBounce 0.37.0** 二次开发而来。

原版 LiquidBounce 功能庞杂，且整体偏向 PVP 对抗场景，其中相当一部分模块并不适合日常游玩与角色扮演环境。LokoAmi 做的是一次**减法**：拆掉高风险的对抗类模块，保留真正顺手的操作辅助，再把视觉与交互重做一遍。

标识是一枚**左倾 45° 的樱花瓣**，配色为**深海蓝黑 + 玫粉**。

## 当前版本：V0.1

**改名与视觉重构**

- 资源命名空间由 `liquidbounce` 统一迁移至 `lokoami`（模组 ID、配置目录、主题资源包同步）
- 全新标识与启动页署名区（贡献清单 · 开发 lie_fish · 版本号 V0.1）
- 全新配色：`#0A1628` 深海蓝黑 / `#F0508C` 玫粉 / `#EAF2FF` 主文字
- 新增 **无限夜视**、**自动疾跑**
- 新增 **Custom** 分类（6 个模块空壳占位）
- 移除高风险对抗类模块：mixin 注入点 **158 → 138**，清理 **473 处**悬空引用

## 安装

1. 安装 **Fabric Loader**（≥ 0.19.x）
2. 游戏版本需为 **Minecraft 1.21.11**
3. 从 [Releases](https://github.com/caishi490-pixel/LokoAmi/releases) 下载 `LokoAmi-0.1.jar`
4. 放入 `.minecraft/mods/`
5. 确认两个前置模组同样位于 `mods/`：
   - [Fabric API](https://modrinth.com/mod/fabric-api) `0.141.6+1.21.11`
   - [Fabric Language Kotlin](https://modrinth.com/mod/fabric-language-kotlin) `1.13.13+kotlin.2.4.10`
6. 启动游戏

> ⚠️ 因客户端名已变更，首次启动会生成**全新的配置目录 `LokoAmi\`**，原 `LiquidBounce\` 下的旧配置不会自动迁移。

## 从源码构建

需要 **JDK 21** 与 **Node.js**（用于构建界面主题）。

```bash
git clone https://github.com/caishi490-pixel/LokoAmi.git
cd LokoAmi
./gradlew assemble
```

产物位于 `build/libs/LokoAmi-0.1.jar`。

开发时可用 `./gradlew runClient` 直接启动，`./gradlew genSources` 生成可读的 Minecraft 源码（可选，便于调试）。

> **踩坑提示**：Kotlin 的 `internal` 成员在 JVM 上会带编译期模块名后缀，模块名跟随 Gradle `rootProject.name`。
> 本项目 `rootProject.name = "LokoAmi"`，故后缀为 `$LokoAmi`。若改动 `settings.gradle.kts` 中的
> `rootProject.name`，必须同步修改 `MixinPacketWrapper.java` 中对
> `InventoryManager.setInventoryOpenServerSide$LokoAmi(...)` 的调用。
> 全项目仅此 1 处 Java 侧引用该混淆名，漏改会直接导致 `:compileJava` 报「找不到符号」。

## 已知问题

- 启动页仅完成标识与配色替换，版式仍沿用原版布局
- 6 个自定义模块为空壳，暂无功能
- ClickGUI / HUD 仅有配色改动，未做结构级重构
- V0.1 尚未经过大规模实机验证

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
