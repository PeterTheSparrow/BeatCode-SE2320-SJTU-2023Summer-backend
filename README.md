# BeatCode-backend

本仓库为BeatCode项目的后端代码。本项目为上海交通大学软件学院2023年暑期课程《互联网产品设计与开发》课程项目。

链接：

| 仓库名称 | 仓库地址 |
| :---: | :---: |
| 项目仓库 | https://github.com/PeterTheSparrow/BeatCode-SE2320-SJTU-2023Summer |
| 前端 | https://github.com/PeterTheSparrow/BeatCode-SE2320-SJTU-2023Summer-frontend |
| 后端 | https://github.com/PeterTheSparrow/BeatCode-SE2320-SJTU-2023Summer-backend  |

## 后端架构简介

BeatCode后端采用Spring Cloud Eureka微服务架构，分为如下几个微服务：
- auth：鉴权服务
- judge：评测服务
- qbank：题库服务
- user：用户服务
- submission：提交评测服务
- eureka-server：服务注册中心
- consumer：消费者服务；通过服务发现机制（如 Eureka、Consul 等）来查找和调用其他微服务的实例，以完成其业务逻辑。它们可能会向服务注册中心查询可用的服务实例，并根据需要向这些实例发出请求。
