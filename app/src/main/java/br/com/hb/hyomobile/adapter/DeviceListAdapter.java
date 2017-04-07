package br.com.hb.hyomobile.adapter;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.List;

import br.com.hb.hyomobile.R;
import br.com.hb.hyomobile.db.dao.BeaconPersonDAO;

/**
 * Created by vanderson on 14/03/2017.
 */

public class DeviceListAdapter extends ArrayAdapter<DeviceItem> {

    private BluetoothAdapter bTAdapter;
    private List<DeviceItem> lista = null;
    private Context context;
    boolean isShow = true;
    private int res = 0;
    private BeaconPersonDAO dao;




    public DeviceListAdapter(Context context, int resource, List<DeviceItem> lista, BeaconPersonDAO dao) {
        super(context, resource, lista);
        this.res = resource;
        this.context = context;
        this.lista = lista;
        this.dao = dao;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rowView = convertView;

        if (rowView == null) {
            LayoutInflater layoutItem = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = layoutItem.inflate(res, null);
        }
        final DeviceItem item = lista.get(position);
        String name = "Nome: "+ (item.getPerson().getName()!= null ? item.getPerson().getName(): "");
        TextView textPerson = (TextView) rowView.findViewById(R.id.textPerson);
        textPerson.setText(name);

        TextView txtRssi = (TextView) rowView.findViewById(R.id.txtRssi);
        txtRssi.setText("RSSI: "+String.valueOf(item.getRssi()));
        TextView txtMacAddress = (TextView) rowView.findViewById(R.id.txtMacDevice);
        txtMacAddress.setText("Mac Address: "+item.getAddress());

        DecimalFormat df = new DecimalFormat("#.##");
        TextView txtDistance = (TextView) rowView.findViewById(R.id.txtDistance);
        txtDistance.setText("Distancia: " + df.format(item.getDistance())+" metros");

        rowView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                new AlertDialog.Builder(getContext())
                        .setMessage("Adicionar device: "+item.getAddress()+" a lista de acompanhamento?")
                        .setCancelable(false)
                        .setPositiveButton(v.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dao.gravar(item.getPerson());

                                dialog.dismiss();
                                Snackbar.make(v, "Device: "+item.getAddress()+" adicionado com Sucesso.", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        })
                        .setNegativeButton(v.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                return true;
            }
        });

        return rowView;
    }

    @Override
    public void clear() {
        lista.clear();
    }

    @Override
    public void addAll(@NonNull Collection<? extends DeviceItem> collection) {
        lista.addAll(collection);
    }

    @Override
    public void add(@Nullable DeviceItem object) {
        lista.add(object);
        //this.notifyDataSetChanged();

    }

}
