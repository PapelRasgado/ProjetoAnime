package jp.com.projetoanime;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * Created by João Paulo on 19/02/2018.
 */

public class TelaView extends AppCompatActivity {

    final String FILENAME = "animes_lista";

    int animeP;

    File file;

    List<Anime> animes;
    ImageButton editBtn;
    Button btnConc;
    EditText nome;
    EditText ep;
    EditText temp;
    EditText notas;
    EditText url;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_view);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        animeP = getIntent().getIntExtra("position", -1) ;

        file = getFileStreamPath(FILENAME);


        try{
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            animes = (List<Anime>) ois.readObject();
            fis.close();
            ois.close();
        }catch(Exception e){

        }

        editBtn = findViewById(R.id.edit_btn);
        btnConc = findViewById(R.id.btn_conc_edit);
        nome = findViewById(R.id.nome_edit);
        ep = findViewById(R.id.ep_edit);
        temp = findViewById(R.id.temp_edit);
        notas = findViewById(R.id.notas_edit);
        url = findViewById(R.id.url_edit);

        final Anime anime = animes.get(animeP);

        nome.setText(anime.getNome());
        ep.setText(anime.getEp() + "");
        temp.setText(anime.getTemp() + "");
        notas.setText(anime.getNotas());
        url.setText(anime.getImage());

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editBtn.setVisibility(View.GONE);
                editBtn.setEnabled(false);
                btnConc.setVisibility(View.VISIBLE);
                btnConc.setEnabled(true);

                nome.setEnabled(true);
                ep.setEnabled(true);
                temp.setEnabled(true);
                notas.setEnabled(true);
                url.setEnabled(true);
            }
        });

        btnConc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nome.getText().length() > 0 && ep.getText().length() > 0){
                    anime.setNome(nome.getText().toString());
                    anime.setEp(Integer.parseInt(ep.getText().toString()));

                    // Pra temporada
                    if (temp.getText().length() > 0){
                        anime.setTemp(Integer.parseInt(temp.getText().toString()));
                    } else {
                        anime.setTemp(1);
                    }

                    anime.setNotas(notas.getText().toString());

                    if (url.getText().length() > 0){

                        anime.setImage(url.getText().toString());
                    } else {
                        anime.setImage(null);
                    }

                    anime.setImage(url.getText().toString());

                    try{
                        FileOutputStream fos = new FileOutputStream(file);
                        ObjectOutputStream oos = new ObjectOutputStream(fos);
                        oos.writeObject(animes);
                        oos.close();
                        fos.close();
                    }catch(Exception e){
                    }

                    recreate();

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
                Intent it = new Intent(TelaView.this, MainActivity.class);
                startActivity(it);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
