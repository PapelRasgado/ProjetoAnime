package jp.com.projetoanime;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;

/**
 * Created by João Paulo on 27/02/2018.
 */

public class TelaCompleta extends AppCompatActivity {

    ListView lista;
    AdapterConc adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_conc);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        lista = findViewById(R.id.lista_conc);

        adapter = new AdapterConc(this);

        lista.setAdapter(adapter);


    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finishAffinity();
                Intent it = new Intent(this, MainActivity.class);
                startActivity(it);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
