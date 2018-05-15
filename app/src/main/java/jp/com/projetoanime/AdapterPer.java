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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by João Paulo on 09/02/2018.
 */

public class AdapterPer extends BaseAdapter {

    private final List<Anime> elementos;
    private final Activity act;
    private final String FILENAME1 = "animes_lista";
    private final String FILENAME2 = "conc_lista";
    private  File fileAnime;
    private final File fileConc;

    public AdapterPer(Activity act) {

        this.act = act;
        fileAnime = act.getFileStreamPath(FILENAME1);
        fileConc = act.getFileStreamPath(FILENAME2);
        List<Anime> elementos = new ArrayList<>();
        try{
            FileInputStream fis = new FileInputStream(fileAnime);
            ObjectInputStream ois = new ObjectInputStream(fis);
            elementos = (List<Anime>) ois.readObject();
            fis.close();
            ois.close();
        }catch(Exception e) {
            Toast.makeText(act, "Deu pau no load", Toast.LENGTH_SHORT).show();
        }
        this.elementos = elementos;
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
        final Anime anime;
        //use convertView recycle
        if(convertView==null){
            holder = new ViewHolder() {
            };
            convertView = LayoutInflater.from(act).inflate(R.layout.values, parent, false);
            holder.nome = convertView.findViewById(R.id.conc_nome);
            holder.image = convertView.findViewById(R.id.conc_imagem);
            holder.notas = convertView.findViewById(R.id.exemplo_notas);
            holder.btnMaisEp = convertView.findViewById(R.id.exemplo_mais);
            holder.btnMenosEp = convertView.findViewById(R.id.exemplo_menos);
            holder.ep = convertView.findViewById(R.id.exemplo_ep);
            holder.epNum = convertView.findViewById((R.id.exemplo_ep_num));
            holder.btnExcluir = convertView.findViewById(R.id.exemplo_excluir);
            holder.btnEdit = convertView.findViewById(R.id.exemplo_edit);
            holder.btnMaisTemp = convertView.findViewById(R.id.exemplo_mais_temp);
            holder.btnMenosTemp = convertView.findViewById(R.id.exemplo_menos_temp);
            holder.temp = convertView.findViewById(R.id.exemplo_temp);
            holder.tempNum = convertView.findViewById(R.id.exemplo_temp_num);
            holder.btnSend = convertView.findViewById(R.id.exemplo_send);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        //set values
        anime = elementos.get(position);

        //set imagem
        if (anime.getImage() != null && anime.getImage().length() > 0) {
            Glide.with(act)
                    .load(anime.getImage())
                    .into(holder.image);
        } else {
            Glide.with(act)
                    .load("@android:color/background_light")
                    .into(holder.image);
        }

        //set TextViews
        holder.nome.setText("Nome: " + anime.getNome());
        holder.epNum.setText(anime.getEp() + "");
        holder.tempNum.setText(anime.getTemp() + "");
        holder.notas.setText("Notas: " + anime.getNotas());

        //set Buttons
        holder.btnMaisEp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                int numEp = Integer.parseInt(holder.epNum.getText().toString());

                anime.setEp(anime.getEp() + 1);
                holder.epNum.setText((numEp + 1) + "");
                save(fileAnime, elementos);

            }
        });

        holder.btnMenosEp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                int numEp = Integer.parseInt(holder.epNum.getText().toString());

                if (numEp != 0) {
                    anime.setEp(anime.getEp() - 1);
                    holder.epNum.setText((numEp - 1) + "");
                    save(fileAnime, elementos);
                }
            }
        });

        holder.btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = act.getLayoutInflater();
                View conView = inflater.inflate(R.layout.apagar_layout, null);
                TextView texto = conView.findViewById(R.id.txt_del);
                texto.setText("Apagar anime!?!?");
                TextView nome = conView.findViewById(R.id.nome_del);
                nome .setText("Quer deletar o anime: " + anime.getNome() + "?");
                new AlertDialog.Builder(act)
                        .setView(conView)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                elementos.remove(anime);
                                save(fileAnime, elementos);
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

        holder.nome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) act.getSystemService(act.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Nome do anime", anime.getNome());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(act, "Nome copiado!", Toast.LENGTH_SHORT).show();
            }
        });

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = act.getLayoutInflater();
                View conView = inflater.inflate(R.layout.layout_view, null);
                ImageView image = conView.findViewById(R.id.img_view);
                Glide.with(act)
                        .load(anime.getImage())
                        .into(image);
                TextView nome = conView.findViewById(R.id.txt_view_nome);
                nome.setText("Nome: " + anime.getNome());

                TextView nota = conView.findViewById(R.id.txt_view_notas);
                nota.setText("Notas: " + anime.getNotas());

                TextView info = conView.findViewById(R.id.txt_view_info);
                info.setText("Episódio: " + anime.getEp() + "  /  Temporada: " + anime.getTemp());

                final AlertDialog d = new AlertDialog.Builder(act)
                        .setView(conView)
                        .create();

                TextView txtButton = conView.findViewById(R.id.txt_btn);
                txtButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                    }
                });
                d.show();

            }
        });

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(act, TelaEdit.class);
                it.putExtra("position", elementos.indexOf(anime));
                act.startActivity(it);
            }
        });

        holder.btnMenosTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int numTemp = Integer.parseInt(holder.tempNum.getText().toString());

                if (numTemp != 0) {
                    anime.setTemp(anime.getTemp() - 1);
                    holder.tempNum.setText((numTemp - 1) + "");
                    save(fileAnime, elementos);
                }
            }
        });

        holder.btnMaisTemp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                int numTemp = Integer.parseInt(holder.tempNum.getText().toString());

                anime.setTemp(anime.getTemp() + 1);
                holder.tempNum.setText((numTemp + 1) + "");
                save(fileAnime, elementos);
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
                nome .setText("Quer enviar o seguinte anime para os concluidos: " + anime.getNome() + "?");
                new AlertDialog.Builder(act)
                        .setView(conView)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                List<Anime> concluidos = new ArrayList<>();
                                try{
                                    FileInputStream fis = new FileInputStream(fileConc);
                                    ObjectInputStream ois = new ObjectInputStream(fis);
                                    concluidos = (List<Anime>) ois.readObject();
                                    fis.close();
                                    ois.close();
                                }catch(Exception e) {
                                    Toast.makeText(act, "Deu pau no load", Toast.LENGTH_SHORT).show();
                                }
                                concluidos.add(anime);
                                elementos.remove(anime);
                                save(fileAnime, elementos);
                                save(fileConc, concluidos);
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

    private Boolean save(File fileName, List<Anime> elementos){
        try{
            FileOutputStream fos = new FileOutputStream(fileName);
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
