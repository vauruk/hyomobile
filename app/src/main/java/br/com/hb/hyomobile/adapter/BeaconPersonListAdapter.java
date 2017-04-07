package br.com.hb.hyomobile.adapter;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import br.com.hb.hyomobile.R;
import br.com.hb.hyomobile.db.dao.BeaconPersonDAO;
import br.com.hb.hyomobile.db.model.BeaconPerson;
import br.com.hb.hyomobile.util.TimeAgo;

/**
 * Created by vanderson on 21/03/2017.
 */

public class BeaconPersonListAdapter extends ArrayAdapter<BeaconPerson> {

    private List<BeaconPerson> list = null;
    private Context context;
    private BeaconPersonDAO dao;
    private int res = 0;


    public BeaconPersonListAdapter(@NonNull Context context, @LayoutRes int itemBeaconPerson, @NonNull List<BeaconPerson> list, BeaconPersonDAO dao) {
        super(context, itemBeaconPerson, list);
        this.list = list;
        this.context = context;
        this.res = itemBeaconPerson;
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
        final BeaconPerson item = list.get(position);

        TextView txtName = (TextView) rowView.findViewById(R.id.txtName);
        txtName.setText("Nome: "+item.getName());

        TextView txtBeaconAddress = (TextView) rowView.findViewById(R.id.txtBeaconAddress);
        txtBeaconAddress.setText("Beacon: "+item.getBeaconAddress());

        //DecimalFormat df = new DecimalFormat("#.##");

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        Date dateIn = null;
        try {
            dateIn = (Date) formatter.parse(item.getDataIn());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String ago = TimeAgo.toRelative(dateIn, TimeAgo.TODAY);
        TextView txtDateIn = (TextView) rowView.findViewById(R.id.txtDateIn);
        txtDateIn.setText("Permanência: "+ago);


        rowView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setMessage("Você tem certeza em excluir esse registro?")
                        .setCancelable(false)
                        .setPositiveButton(v.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dao.excluir(item);
                                remove(item);
                                dialog.dismiss();
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
}
