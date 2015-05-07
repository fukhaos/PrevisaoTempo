package br.unipar.previsaotempo;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class MainActivity extends ActionBarActivity {

    private ActionProcessButton btnSend;
    private EditText cidade;
    private TextView temperatura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        btnSend = (ActionProcessButton) findViewById(R.id.botao);
        cidade = (EditText) findViewById(R.id.cidade);
        temperatura = (TextView) findViewById(R.id.temperatura);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void buscar(View view) {

        String nomeCidade = cidade.getText().toString();
        if (nomeCidade.isEmpty()) {
            cidade.setError("Preencha a cidade");
        } else {

            btnSend.setMode(ActionProcessButton.Mode.ENDLESS);
            btnSend.setProgress(1);

            Ion.with(MainActivity.this)
                    .load("http://www.previsaodotempo.org/api.php?city=" + nomeCidade)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {

                            if (e == null) {
                                btnSend.setProgress(100);
                                JsonObject data = result.get("data").getAsJsonObject();
                                temperatura.setText(data.get("temperature").getAsString() + "º");
                            } else {
                                btnSend.setProgress(-1);
                            }
                        }
                    });

        }
    }
}
