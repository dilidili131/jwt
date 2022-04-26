# jwt
springboot整合jwt

## 一、导入jwt依赖
```xml
<dependency>
	<groupId>com.auth0</groupId>
    <artifactId>java-jwt</artifactId>
    <version>3.18.2</version>
</dependency>
```

## 二、新建工具类
![在这里插入图片描述](https://img-blog.csdnimg.cn/bb2f07200e714a98ad860cf4e50effe6.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAZGlsaWRpbGkxMzE=,size_16,color_FFFFFF,t_70,g_se,x_16)

```java
public class JWTUtils {
    //用于加密header和payload形成signature，引号内自己指定
    private static final String SIGN = "qwe123!@#";

    //用于获取token
    public static String getToken(Map<String,String> map){
        //用于指定token过期日期，此处指定为3天
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE,3);

        JWTCreator.Builder builder = JWT.create();
        //map用于指定payload内容
        map.forEach((k,v)->{
            builder.withClaim(k,v);
        });
        String token = builder.withExpiresAt(instance.getTime())//指定token过期时间
                .sign(Algorithm.HMAC256(SIGN));//指定算法

        return token;
    }

    //用于验证token并返回token中的信息
    public static DecodedJWT verify(String token){
        return JWT.require(Algorithm.HMAC256(SIGN)).build().verify(token);
    }
}

```
## 二、新建拦截器
![在这里插入图片描述](https://img-blog.csdnimg.cn/0077609089dc4370822425c2644e909b.png)

新建拦截器实现HandlerInterceptor类并重些preHandle函数。
```java
public class JWTInterceptors implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取header中的token
        String token = request.getHeader("token");
        Map<String,Object> map = new HashMap<>();
        try {
            DecodedJWT verify = JWTUtils.verify(token);
            return true;
        }catch (SignatureVerificationException e){
            //无效签名
            e.printStackTrace();
            map.put("msg","无效签名");
        }catch (TokenExpiredException e){
            //token过期
            e.printStackTrace();
            map.put("msg","token过期");
        }catch(AlgorithmMismatchException e){
            //token算法不一致
            e.printStackTrace();
            map.put("msg","token算法不一致");
        }catch (Exception e){
            e.printStackTrace();
            map.put("msg","token无效");
        }
        map.put("state",false);
        //将map转为json （通过jackson）
        String json = new ObjectMapper().writeValueAsString(map);
        //将错误信息写入响应体
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(json);
        return false;
    }
}
```
## 三、配置工具类
![在这里插入图片描述](https://img-blog.csdnimg.cn/71d8159aeca94d7cb9dc885c3c3ef439.png)

```java
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JWTInterceptors())
                .addPathPatterns("/**")//需要jwt验证的路径
                .excludePathPatterns("/user/login");//不需要jwt验证的路径
    }
}
```
**大功告成**
## 测试
新建jwt数据库，user表
简易实现登录功能
调用login接口获取token
![在这里插入图片描述](https://img-blog.csdnimg.cn/f7a6cdeaca3d4f0ea5c5043e5d6b8267.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAZGlsaWRpbGkxMzE=,size_20,color_FFFFFF,t_70,g_se,x_16)
调用其他接口原则上必须在header中指定token，若不指定将返回错误信息
![在这里插入图片描述](https://img-blog.csdnimg.cn/1cbb434ce8174ebeb0cc845f3c40b109.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAZGlsaWRpbGkxMzE=,size_20,color_FFFFFF,t_70,g_se,x_16)
传入正确token则返回正确信息
![在这里插入图片描述](https://img-blog.csdnimg.cn/244b3f2a821b4df4be12de19210f6530.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAZGlsaWRpbGkxMzE=,size_20,color_FFFFFF,t_70,g_se,x_16)
