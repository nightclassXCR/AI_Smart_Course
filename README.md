# 智慧教学平台

## 项目简介

智慧教学平台是一款面向教师与学生的智能化教学管理系统，集成了课程管理、章节管理、任务作业、资源管理、知识图谱、学习分析、AI智能学伴与AI心理辅导等多项功能，旨在提升教学效率与学习体验。

## 项目背景与目标

随着教育信息化的不断推进，传统教学方式已难以满足现代教育的个性化和智能化需求。智慧教学平台致力于为教师和学生提供一站式、智能化的教学与学习支持，帮助教师高效管理课程、分析学生学习情况，帮助学生自主学习、获得个性化辅导和心理健康关怀。

## 技术架构与核心技术栈

- **后端**：Java 17、Spring Boot、MyBatis、Spring Security、JWT、Redis、MySQL
- **前端**：Vue + Vite、Element UI、Apache ECharts
- **AI能力**：集成AI问答、智能推荐、心理健康分析等模块
- **其他**：Docker、Maven、邮件服务、对象存储（OSS）等

## 主要模块详细介绍

### 1. 用户与权限管理

- 支持手机号、邮箱注册与登录，找回密码
- JWT安全认证，分为教师端与学生端，权限分明

### 2. 课程与章节管理

- 教师可创建、编辑、删除课程及其章节
- 学生可选课、查看课程结构与内容

### 3. 任务与作业管理

- 教师布置任务、作业，支持批量导入题库
- 学生在线提交作业，系统自动批改与反馈

### 4. 资源管理

- 支持多种类型的教学资源上传、下载与管理
- 资源与课程、章节关联，便于查找

### 5. 知识图谱

- 构建课程知识点之间的关联关系
- 可视化展示知识网络，辅助学生系统化学习

### 6. 学习分析

- 统计学生学习行为、成绩、作业完成情况
- （规划中）生成个性化学习报告，辅助教师因材施教

### 7. AI智能学伴

- 提供个性化学习建议、自动答疑、学习路径推荐
- 支持自然语言交互，提升学习体验

### 8. AI心理辅导

- 集成AI心理健康助手，定期心理测评与关怀
- 提供心理健康建议与危机预警

## 安装部署指南

1. **环境准备**

   - JDK 17 及以上
   - Maven 3.6 及以上
   - MySQL 5.7/8.0 数据库
   - Redis（如需缓存支持）
   - 推荐使用 Linux/Windows 服务器

2. **克隆项目**

   ```bash
   git clone https://github.com/nightclassXCR/AI_Smart_Course.git
   cd AI_Smart_Course
   ```

3. **配置数据库与环境变量**

   - 添加 `src/main/resources/application-dev.properties`，配置数据库、Redis、邮件等相关信息。

4. **构建与运行**

   - 使用 Maven 构建项目：

     ```bash
     mvn clean package
     ```

   - 运行 Spring Boot 应用：

     ```bash
     java -jar target/ai_smart_course-0.0.1-SNAPSHOT.jar
     ```

   - 或使用 IDE 直接运行 `AiSmartCourseApplication.java`

5. **前端**

   - 请参考前端子项目或相关文档进行部署。
   - 前端地址：https://github.com/nightclassXCR/AI_Smart_Course_front

## 使用说明

- 访问平台首页，选择教师端或学生端登录。
- 教师可进行课程、章节、任务、资源等管理，查看学习分析报告。
- 学生可选课、查看课程内容、提交作业、参与测试、与AI学伴互动。
- AI心理辅导功能可在个人中心或专属入口访问。
- 详细操作请参考系统内帮助文档或联系管理员。

## 常见问题（FAQ）

**Q1：启动时报数据库连接错误怎么办？**
A：请检查 `application.properties`与`application-dev.properties` 中配置是否正确，数据库服务是否已启动。

**Q2：如何修改默认端口？**
A：在 `application.properties` 中修改 `server.port` 配置项。

**Q3：AI相关功能无法使用？**
A：请检查AI服务相关配置，确保网络畅通及API Key等参数正确。

**Q4：如何添加新课程/章节/题库？**
A：登录教师端，进入相应管理页面，按提示操作即可。

## 贡献指南

欢迎各界开发者参与智慧教学平台的开发与优化！

1. Fork 本仓库并创建分支
2. 提交代码前请确保通过所有单元测试
3. 提交 Pull Request 并详细描述修改内容
4. 遵循项目代码规范与贡献流程

## 开发成员及分工

- **nightclasXCR**：课程管理，学习分析，知识图谱，AI智能学伴，AI心理辅导

- **fengqi66**：任务管理，资源管理，AI智能学伴，AI心理辅导

- **liang-zzh**：作业管理、题库管理与答题功能，单元测试

- **Aaron-zjh-coder**：单元测试，系统性能测试，学习分析

- **Arrhenius401**：用户登录、手机号邮箱注册，找回密码，jwt校验，学习分析



## 开源仓库地址

[https://github.com/nightclassXCR/AI_Smart_Course](https://github.com/nightclassXCR/AI_Smart_Course) 
