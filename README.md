生成 jenkins Report ， 会根据输入的jenkins 地址，遍历所有项目，并按项目、文件夹、整个服务器获得统计数据




文件夹的统计数据是包括文件夹下的所有 job, 包括自文件夹下的job .

## 添加 job 类型
- 目前只支持 文件夹 和 WorkflowJob， 目前不确认是否还有其他类型
- 要添加新的 job 类型，需要
  1. 添加新的类继承 job 
  2. 添加创建新 job 对象的逻辑分支到 FolderJob 的构造方法
  
 # 打包
 mvn clean package
  
  
 ## 运行
 java -jar -Durl=http://host/ -Duser=Alex -Dpassword=123 -Dfrom="2019-9-10 13:00:00" -Dto=2020-1-8 App.jar
 
java -jar -Dpassword=yoogodong -Dfrom='2019-12-30 00:00:00'  App.jar 
  
  
 ## 几个指标的算法
- 平均构建时间： 成功的构建总时长/成功构建次数， 构建时长包含需要人输入的等待时间
- 
 

