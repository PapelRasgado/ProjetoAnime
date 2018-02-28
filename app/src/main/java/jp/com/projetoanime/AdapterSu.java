package jp.com.projetoanime;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * Created by João Paulo on 26/02/2018.
 */

public class AdapterSu extends BaseAdapter {

    private final List<String> elementos;
    private final Activity act;
    final String FILENAME = "sugestao_lista";
    final File file;

    public AdapterSu(List<String> elementos, Activity act) {
        this.elementos = elementos;
        this.act = act;
        file = act.getFileStreamPath(FILENAME);
    }

    @Override
    public int getCount() {
        return elementos.size();
    }

    @Override
    public Object getItem(int position) {
        return elementos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final String sugestao;
        //use convertView recycle
        if(convertView==null){
            holder = new ViewHolder() {
            };
            convertView = LayoutInflater.from(act).inflate(R.layout.values_sugestao, parent, false);
            holder.nome = convertView.findViewById(R.id.su_nome);
            holder.btnExcluir = convertView.findViewById(R.id.su_del);
            holder.btnSend = convertView.findViewById(R.id.su_send);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        //set values
        sugestao = elementos.get(position);

        holder.nome.setText(sugestao);
        holder.nome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) act.getSystemService(act.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Nome do anime", sugestao);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(act, "Nome copiado!", Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = act.getLayoutInflater();
                View conView = inflater.inflate(R.layout.apagar_layout, null);
                TextView texto = conView.findViewById(R.id.txt_del);
                texto.setText("Apagar sugestão!?!?");
                TextView nome = conView.findViewById(R.id.nome_del);
                nome .setText("Quer deletar a sugestão: " + sugestao + "?");
                new AlertDialog.Builder(act)
                        .setView(conView)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                elementos.remove(sugestao);
                                save();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .show();
            }
        });

        holder.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater inflater = act.getLayoutInflater();
                View conView = inflater.inflate(R.layout.apagar_layout, null);
                TextView texto = conView.findViewById(R.id.txt_del);
                texto.setText("Enviar anime!?!?");
                TextView nome = conView.findViewById(R.id.nome_del);
                nome .setText("Quer enviar o seguinte anime para a edição: " + sugestao + "?");
                new AlertDialog.Builder(act)
                        .setView(conView)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(act, TelaCadastro.class);
                                intent.putExtra("sugestao", sugestao);
                                elementos.remove(sugestao);
                                save();
                                act.finishAffinity();
                                act.startActivity(intent);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .show();
            }
        });

        return convertView;
    }

    private Boolean save(){
        try{
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(elementos);
            oos.close();
            fos.close();
            act.recreate();
            return true;
        }catch(Exception e){
            return false;
        }
    }
}
