package com.example.edwin.applogin;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpClientConnection;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultHttpClientConnection;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    EditText txtUsuario,txtPassword;
    Button btnIngresar,btnSalir;
    LoginService loginService;
    LogoutService logoutService;
    Integer userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtUsuario = (EditText) findViewById(R.id.txtUsuario);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        btnIngresar = (Button) findViewById(R.id.btnIngresar);
        btnSalir = (Button) findViewById(R.id.btnSalir);

        btnIngresar.setOnClickListener(this);
        btnSalir.setOnClickListener(this);
        /*
        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario = txtUsuario.getText().toString();
                String password = txtPassword.getText().toString();

                //Consultando Servicio Json
                loginService = new LoginService();
                loginService.execute(usuario,password);
                //Fin Consulta Servicio Json

                if(!usuario.equalsIgnoreCase("admin")){
                    Toast.makeText(getApplicationContext(), "Usuario incorrecto", Toast.LENGTH_LONG).show();
                }else if (!password.equalsIgnoreCase("123456")){
                    Toast.makeText(getApplicationContext(), "Contraseña incorrecta", Toast.LENGTH_LONG).show();
                }else {
                    Intent intent =  new Intent(getApplicationContext(),InicioActivity.class);
                    startActivity(intent);
                }
            }
        });
        */

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnIngresar:
                String usuario = txtUsuario.getText().toString();
                String password = txtPassword.getText().toString();

                if(!usuario.equals("") && !password.equals("")){
                    loginService = new LoginService();
                    loginService.execute(usuario, password);

                }else{
                    Toast.makeText(getApplicationContext(),"Debe ingresar usuario y contraseña",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btnSalir:
                logoutService = new LogoutService();
                logoutService.execute(userId);
                break;
            default:
                break;
        }
    }

    private void leerJson(){

    }

    //Revisar documentaci'on de la clase AsyncTask
    private class LoginService extends AsyncTask<String, String, Boolean>{
        JSONObject responseJSON;

        @Override
        protected Boolean doInBackground(String... params) {
            Boolean result = true;
            try{
                Log.v("INFO","Procesando...");

                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://192.168.1.44:8080/android/json.php");
                httpPost.setHeader("content-type", "application/json");

                //HttpGet httpGet = new HttpGet("http://192.168.1.44:8080/android/json.php");

                JSONObject jsonDatos = new JSONObject();
                jsonDatos.put("usuario",params[0]);
                jsonDatos.put("password", params[1]);
                jsonDatos.put("token", "TOKEN_ID");

                StringEntity entity = new StringEntity(jsonDatos.toString());
                httpPost.setEntity(entity);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                //HttpResponse httpResponse = httpClient.execute(httpGet);

                String resultado = EntityUtils.toString(httpResponse.getEntity());

                JSONObject respJSON = new JSONObject(resultado);
                if(!respJSON.getBoolean("exito")){
                    result = false;
                }else{
                    responseJSON = respJSON;
                }

            }catch (Exception ex){
                //ex.printStackTrace();
                Toast.makeText(getApplicationContext(),"Ocurrio un Error:"+ex.getMessage(),Toast.LENGTH_LONG).show();
            }

            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(result == true){
                try {
                    JSONObject datos = responseJSON.getJSONObject("datosUsuario");

                    userId = datos.getInt("idUsuario");
                    datos.getString("nombres");
                    datos.getString("apellidos");

                    Log.v("INFO", "datos.nombres=>" + datos.getString("nombres"));
                    Log.v("INFO","datos.apellidos=>"+datos.getString("apellidos"));

                    Toast.makeText(getApplicationContext(),"Bienvenido: "+ datos.getString("nombreUsuario"),Toast.LENGTH_LONG).show();
                    //txtUsuario.setAlpha(0.0f);
                    btnSalir.setVisibility(View.VISIBLE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class LogoutService extends  AsyncTask<Integer, String, Boolean>{
        JSONObject responseJSON;

        @Override
        protected Boolean doInBackground(Integer... params) {
            Boolean result = true;
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://192.168.1.44:8080/android/json.php");

            httpPost.setHeader("content-type","application/json");

            JSONObject jsonDatos = new JSONObject();
            try {
                jsonDatos.put("idUsuario",params[0]);
                jsonDatos.put("token", "TOKEN_ID");

                StringEntity entity = new StringEntity(jsonDatos.toString());
                httpPost.setEntity(entity);
                HttpResponse httpResponse = httpClient.execute(httpPost);

                String respuesta = EntityUtils.toString(httpResponse.getEntity());
                JSONObject jsonRespuesta = new JSONObject(respuesta);

                if(!jsonDatos.getBoolean("Exito")){
                    result = false;
                }else  {
                    responseJSON = jsonDatos;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if(result){
                txtUsuario.setText("");
                txtPassword.setText("");

                btnSalir.setVisibility(View.INVISIBLE);
            }
        }
    }

    private StringBuilder inputStreamToString(InputStream input){
        String rline = "";
        StringBuilder sb = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(input));

        try{
            while ((rline = rd.readLine()) != null){
                sb.append(rline);
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return sb;
    }
}

