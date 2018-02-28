package jp.com.projetoanime;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by João Paulo on 27/02/2018.
 */

public class AdapterConc extends BaseAdapter{

    private final List<Anime> elementos;
    private final Activity act;
    final String FILENAME1 = "animes_lista";
    final String FILENAME2 = "conc_lista";
    final File fileAnime;
    final File fileConc;

    public AdapterConc(Activity act) {

        this.act = act;
        fileAnime = act.getFileStreamPath(FILENAME1);
        fileConc = act.getFileStreamPath(FILENAME2);
        List<Anime> elementos = new ArrayList<>();
        try{
            FileInputStream fis = new FileInputStream(fileConc);
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
        if (convertView == null) {
            holder = new ViewHolder() {
            };
            convertView = LayoutInflater.from(act).inflate(R.layout.values_completo, parent, false);
            holder.image = convertView.findViewById(R.id.conc_imagem);
            holder.btnExcluir = convertView.findViewById(R.id.conc_del);
            holder.nome = convertView.findViewById(R.id.conc_nome);
            holder.ep = convertView.findViewById(R.id.conc_ep);
            holder.temp = convertView.findViewById(R.id.conc_temp);
            holder.notas = convertView.findViewById(R.id.conc_notas);
            holder.btnSend = convertView.findViewById(R.id.conc_back);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //set values
        anime = elementos.get(position);

        //set imagem
        if (anime.getImage() != null && anime.getImage().length() > 0) {
            Picasso.with(act).load(anime.getImage()).into(holder.image);
        } else {
            Picasso.with(act).load("@android:color/background_light").into(holder.image);
        }

        //set TextViews
        holder.nome.setText("Nome: " + anime.getNome());
        holder.ep.setText("Episodio: " + anime.getEp());
        holder.temp.setText("Temporada: " + anime.getTemp());
        holder.notas.setText("Notas: " + anime.getNotas());

        //set Buttons

        holder.btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = act.getLayoutInflater();
                View conView = inflater.inflate(R.layout.apagar_layout, null);
                TextView texto = conView.findViewById(R.id.txt_del);
                texto.setText("Apagar Anime!?!?");
                TextView nome = conView.findViewById(R.id.nome_del);
                nome .setText("Quer deletar a anime: " + anime.getNome() + "?");
                new AlertDialog.Builder(act)
                        .setView(conView)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                elementos.remove(anime);
                                save(fileConc, elementos);
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
                    texto.setText("Enviar Anime!?!?");
                TextView nome = conView.findViewById(R.id.nome_del);
                nome .setText("Quer enviar de volta o seguinte anime: " + anime.getNome() + "?");
                new AlertDialog.Builder(act)
                        .setView(conView)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                List<Anime> animes = new ArrayList<>();
                                try{
                                    FileInputStream fis = new FileInputStream(fileAnime);
                                    ObjectInputStream ois = new ObjectInputStream(fis);
                                    animes = (List<Anime>) ois.readObject();
                                    fis.close();
                                    ois.close();
                                }catch(Exception e) {
                                    Toast.makeText(act, "Deu pau no load", Toast.LENGTH_SHORT).show();
                                }
                                animes.add(anime);
                                save(fileAnime, animes);
                                elementos.remove(anime);
                                save(fileConc, elementos);
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

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) act.getSystemService(act.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Nome do anime", anime.getNome());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(act, "Nome copiado!", Toast.LENGTH_SHORT).show();
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
