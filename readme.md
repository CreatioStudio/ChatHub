<!-- markdownlint-disable-file MD024 -->
# Chat Hub

![icon](icon.png)

> Bungeecord 跨服聊天插件

在 Bungeecord 服务器中跨服聊天

![screenshot1](screenshot1.jpg)

## 功能

- 纯 Bungeecord 插件，各子服聊天互通
- 可配置消息事件格式、服务器名、直达服务器格式
- 多服或多 BC 间互通消息、私聊
- 彩色聊天
- QQ互通
- 敏感词屏蔽（默认无词库，需自行配置）

## 安装

1.在 Release 页面下载最新版插件，放入 Bungeecord 服务器的 `plugins` 文件夹内
1.重启 Bungeecord
1.修改 `plugins/ChatHub/config.yml`
1.使用 `chat reload` 指令重载配置文件

## 指令

### 游戏内指令

| 指令 | 功能 |
| - | - |
| /chat list | 查看所有服务器的玩家列表 |
| /chat msg \<name\> \<msg\> | 向玩家发送私聊消息 |
| /chat qq \<msg\> | 向 QQ 发送消息 |

### QQ 指令

| 指令 | 功能 |
| - | - |
| /mc \<msg\> | 向游戏内发送消息 |

## 配置文件

配置文件为 `plugins/ChatHub/config.yml`

## message

消息格式

默认值：

```yaml
join: '§8[§a+§8] §e{player}'
leave: '§8[§c-§8] §e{player}'
switch: '§8[§b❖§8] §e{player}§r: §7«{serverFrom}§7» §6➟ §7«{serverTo}§7»'
chat: '§7[{serverOn}§7]§e{player}§r: {message}'
msg_sender: '§7§o你悄悄地对{target}说: {message}'
msg_target: '§7§o{sender}悄悄地对你说: {message}'
block_word: '§8§l» §c你的消息 "{message}" 被禁止发送, 因为包含敏感词 "{blockWord}"'
list: '§8§l» §7[{serverOn}§7]当前共有§6{playerCount}§7名玩家在线: §e{playerList}'
```

## server_name

服务器 ID 和服务器名对照表

默认值：

```yaml
qq: '§5§l群聊天'
```

## port

接收广播和QQ上报的服务器端口

默认值：`51800`

## broadcast

多 BC 间广播消息配置

### token

秘钥

默认值：`token`

### cancel_timeout

取消消息事件时间限制

默认值：`50`

### servers

广播的目标服务器

默认值：

```yaml
- 'host:port'
```

## ignore_rules

忽略处理的聊天消息格式

默认值：

```yaml
  - '!!.*'
```

## block_words

敏感词列表

默认值：

```yaml
  - 'Hello'
```

## qq

QQ 互通设置

### enable

是否启用

默认值：`false`

### path

接收上报消息 URL 路径

默认值：`/qq`

### api_url

QQ 机器人 API URL

默认值：`http://localhost:5700`

### message

是否互相转发消息

默认值：`false`

### group_id

允许的群列表

默认值：

```yaml
  - 123123123
```

## 提示

- 在 Bukkit 中加载此插件，使 Bukkit 插件可以接收消息事件，如箱子商店等

## 开发

你可以接收广播消息来处理一些自定义操作，广播消息的格式和路径请自行翻代码
