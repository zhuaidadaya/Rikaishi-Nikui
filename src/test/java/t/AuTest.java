package t;

public class AuTest {
    public static void main(String[] args) {
        try {
//            HttpClient client = HttpClients.createDefault(); //构建一个Client
//            HttpPost post = new HttpPost(
//                    "https://login.live.com/oauth20_authorize.srf?client_id=00000000402b5328&response_type=code&scope=service%3A%3Auser.auth.xboxlive.com%3A%3AMBI_SSL&redirect_uri=https%3A%2F%2Flogin.live.com%2Foauth20_desktop.srf"
//            );
//构建表单参数
//            List<NameValuePair> formParams = new ArrayList<NameValuePair>();
//            formParams.add(new BasicNameValuePair("username", "yourname"));
//            formParams.add(new BasicNameValuePair("password", "yourpassword"));
//            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formParams, "UTF-8");//将表单参数转化为“实体”
//            post.setEntity(entity);//将“实体“设置到POST请求里
//            HttpResponse response = client.execute(post);//提交POST请求
//            HttpEntity result = response.getEntity();//拿到返回的HttpResponse的"实体"
//            String content = EntityUtils.toString(result);
            //用httpcore.jar提供的工具类将"实体"转化为字符串打印到控制台
//            System.out.println(content);
        } catch (Exception e) {

        }
    }
}
