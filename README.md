# project_space
##黑名单接口
###一、请求接入流程
####1、请求前准备
（1）、商户选择需要接入的接口，并向我方提出申请。

（2）、我方为商户分配唯一的app_id和app_key并开通相应接口权限，请妥善保管好app_key，不要向外泄露。

（3）、商户使用app_id和app_key访问我方提供的接口，获得access_token，该token在一定时间范围内有效，过期后需要重新获取。接口地址如下：
```
http://ip:port/oapi/token?app_id=商户app_id&datetime=请求时间&sign=MD5(datetime+商户app_key+datetime)
```
####2、请求访问接口
（1）、商户使用“datetime+app_key+datetime”的MD5值做为AES加密秘钥，对请求参数信息(JSON格式字符串)进行加密。
（2）、商户使用app_id、access_token和datetime，并带上加密的请求参数发起请求。格式为JSON，示例如下：
```
{
    "app_id" : "您的app_id",
    "access_token":"有效的access_token",
    "datetime" : "请求时间,13位毫秒级，用于进行报文的加密和解密",
    "req_seqno" : "每次请求的流水号，唯一",
    "message" : "使用AES加密的请求参数"
}
```
####3、商户收到应答结果内容后，使用“datetime+app_key+datetime”的MD5值做为AES解密秘钥，对结果进行解密,解密的内容为JSON格式的字符串。
###二、接入步骤
```
具体可见接口代码示例
```
####1. 获取请求时间(datetime)
+ 请求时间为13位的毫秒级字符串
+ 请求时间配合app_key用于对请求参数进行加密和解密.
####2. 封装请求参数(message)
+ 请求参数为JSON格式，参数中一定存在service_id字段，用于识别具体的接口服务。
+ 请求参数需要根据具体的接口规范传入。
####3. 加密请求参数（message）
+ 为了安全性考虑，必须对请求参数进行AES128加密
+ 加密秘钥为MD5（datetime+app_key_datetime），md5值为HEX的小写。
####4. 构建请求JSON
+ 填充app_id
+ 填充access_token
+ 填充datetime
+ 填充req_seqno
+ 填充加密后的message

####5. 发送请求
+ 提交POST请求
####6. 结果解密
+ 对返回的结果数据（字符串）进行AES解密，秘钥为加密请求参数时使用的秘钥。

#附录
##access_token说明
###1、首先介绍几个名词含义：

+ app_id：每个接入商户或应用的唯一ID。
+ access_token：接口访问的凭证，access_token有有效期的限制，有效期的时间由服务器决定，过期后需要重新调用接口获取。
+ app_key：数据加密和解密的凭证，app_key不能对外暴露，如果一旦暴露，需要告知我方进行更改。
+ datetime：请求的时间，13位的毫秒级字符串，配合app_key生成加密秘钥或者签名。
+ req_seqno：请求流水号，客户方业务请求流水号，用于记录跟踪及问题查找。
###2、access_token获取说明

+ 请求方式：HTTP+GET

+ 接口地址：
```
 http://ip:port/oapi/token?app_id=商户app_id&datetime=请求时间&sign=MD5(datetime+商户app_key+datetime)
```
+ 返回格式：JSON

+ 返回字段说明：

字段名称	字段含义	备注
errcode	错误代码	0代表成功
errmsg	错误说明	
access_token	token值	errcode为0时才有数据
expires_in	有效期时间	单位为小时






#内部接口服务说明
##1.反欺诈类
+ 请求方式：http+post
+ 接口地址：
```
 http://ip:port/bigData/antifraudapi/query
```
+ 请求参数：
```
名称          必填    类型         说明
service_id   是      string      服务ID，对应内部的产品ID
idCard       是      string      身份证号 
realName     是      string      姓名
mobile       是      string      手机号码
```

+ 输出参数：
```
名称	         含义	         必填	类型	    说明
retCode	     错误码	         是	    string	0代表成功
retMsg	     错误信息	         是	    string	
transId	     交易流水号	     是	    string	用于交易跟踪
result	     结果信息	         否	    json	retCode为0时才有此信息
blackMatch	 是否匹配风险名单	 是	    int	    0-未匹配,1-匹配
blackReason  风险名单原因	     否	    string	参考下方返回值 blackReason 和 blackDetails 详情解释
blackDetail  风险名单详情	     否	    json	参考下方返回值 blackReason 和 blackDetails 详情解释
```