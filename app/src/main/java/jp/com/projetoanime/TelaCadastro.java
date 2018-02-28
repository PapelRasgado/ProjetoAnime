package jp.com.projetoanime;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by João Paulo on 17/02/2018.
 */

public class TelaCadastro extends AppCompatActivity {

    final String FILENAME = "animes_lista";

    EditText nome;
    EditText ep;
    EditText notas;
    EditText temp;
    Button conc;
    String endereco = null;
    List<Anime> animes = new ArrayList<>();
    File file;
    EditText url;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_cadastro);

        file = getFileStreamPath(FILENAME);


        try{
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            animes = (List<Anime>) ois.readObject();
            fis.close();
            ois.close();
        }catch(Exception e){

        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        nome = findViewById(R.id.nome_edit);
        ep = findViewById(R.id.ep_edit);
        temp = findViewById(R.id.temp_edit);
        notas = findViewById(R.id.notas_edit);
        conc = findViewById(R.id.btn_conc);
        url = findViewById(R.id.url_edit);

        if (getIntent().getExtras() != null){
            if (getIntent().getExtras().getString("sugestao", null) != null) {
                nome.setText(getIntent().getExtras().getString("sugestao"));
            }
        }

        conc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( nome.getText().length() > 0 && ep.getText().length() > 0){
                    String nomeA = nome.getText().toString();
                    int epA = Integer.parseInt(ep.getText().toString());
                    String notaA = notas.getText().toString();
                    int temporada = 1;
                    if(temp.getText().length() > 0){
                        temporada = Integer.parseInt(temp.getText().toString());
                    }
                    if(url.getText().length() > 0){
                        endereco = url.getText().toString();
                    }
                    Anime novo = new Anime(nomeA, epA, notaA, temporada, endereco);
                    animes.add(novo);

                    try{
                        FileOutputStream fos = new FileOutputStream(file);
                        ObjectOutputStream oos = new ObjectOutputStream(fos);
                        oos.writeObject(animes);
                        oos.close();
                        fos.close();
                    }catch(Exception e){
                    }

                    finishAffinity();
                    Intent it = new Intent(TelaCadastro.this, MainActivity.class);
                    startActivity(it);

                } else {
                    Toast.makeText(getApplicationContext(), "Por favor coloque o nome e o ep do anime!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
