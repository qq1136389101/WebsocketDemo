# redis实现Websocket分布式

## 简介
通过redis实现了websocket的分布式使用

## 启动

### 1. 下载项目
```
git clone https://github.com/qq1136389101/WebsocketDemo.git
```

### 2. 安装mvn包
```
mvn install
```
### 3. 启动redis

### 4. 运行项目

## 测试
### 用户登入
`http://localhost/index.html?username=1&groupId=g1`

参数 | 作用
---|---
username | 用户id
groupId | 组id

### 发送消息
#### 发送给单个用户  
`http://localhost/sendMessageToUser?userName=g1&content=测试`

参数 | 作用
---|---
username | 用户id
content | 消息内容


#### 发送给小组用户  
`http://localhost/sendMessageToGroup?groupId=g1&content=测试`

参数 | 作用
---|---
groupId | 小组id
content | 消息内容


