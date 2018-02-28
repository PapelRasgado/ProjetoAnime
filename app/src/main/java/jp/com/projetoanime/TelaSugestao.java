package jp.com.projetoanime;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by João Paulo on 26/02/2018.
 */

public class TelaSugestao extends AppCompatActivity {

    private ListView list;
    private List<String> sugestoes;
    private Button btn;
    private String FILENAME = "sugestao_lista";
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sugestoes_layout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        file = getFileStreamPath(FILENAME);
        sugestoes = new ArrayList<>();

        try{
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            sugestoes = (List<String>) ois.readObject();
            fis.close();
            ois.close();
        }catch(Exception e) {
            Toast.makeText(this, "Deu pau no load", Toast.LENGTH_SHORT).show();
        }

        list = findViewById(R.id.list_su);

        AdapterSu adapter = new AdapterSu(sugestoes, this);

        list.setAdapter(adapter);

        btn = findViewById(R.id.add_su);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = TelaSugestao.this.getLayoutInflater();
                View conView = inflater.inflate(R.layout.input_dialog, null);
                final EditText edt = conView.findViewById(R.id.nome_input);
                final TextView texto = conView.findViewById(R.id.txt_input);
                texto.setText("Qual o nome do anime?");
                new AlertDialog.Builder(TelaSugestao.this)
                        .setView(conView)
                        .setPositiveButton("Comfirmar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (edt.getText().length() > 0) {
                                    sugestoes.add(edt.getText().toString());
                                    save();
                                } else {
                                    Toast.makeText(TelaSugestao.this, "Coloque um nome!!", Toast.LENGTH_SHORT).show();
                                }

                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .show();
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

    private Boolean save(){
        try{
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(sugestoes);
            oos.close();
            fos.close();
            recreate();
            return true;
        }catch(Exception e){
            return false;
        }
    }
}
