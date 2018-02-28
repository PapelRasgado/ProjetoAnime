package jp.com.projetoanime;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;


public class MainActivity extends AppCompatActivity {

    ListView lista;
    AdapterPer adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lista = findViewById(R.id.lista);

        adapter = new AdapterPer(this);

        lista.setAdapter(adapter);

    }

    @Override
    public void onBackPressed(){
        System.exit(0);
    }

    public void proximaTela(View view){
        Intent intent = new Intent(this, TelaCadastro.class);
        startActivity(intent);
    }

    public void proximaTelaSu(View view){
        Intent intent = new Intent(this, TelaSugestao.class);
        startActivity(intent);
    }

    public void proximaTelaCom(View view){
        Intent intent = new Intent(this, TelaCompleta.class);
        startActivity(intent);
    }
}
