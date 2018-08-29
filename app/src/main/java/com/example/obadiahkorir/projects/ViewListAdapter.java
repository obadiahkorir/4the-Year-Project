package com.example.obadiahkorir.projects;

/**
 * Created by Obadiah Korir on 8/2/2018.
 */
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
public class ViewListAdapter extends BaseAdapter
{
    Context context;
    List<fire> valueList;
    public ViewListAdapter(List<fire> listValue, Context context)
    {
        this.context = context;
        this.valueList = listValue;
    }

    @Override
    public int getCount()
    {
        return this.valueList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return this.valueList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewItem viewItem = null;
        if(convertView == null)
        {
            viewItem = new ViewItem();
            LayoutInflater layoutInfiater = (LayoutInflater)this.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            //LayoutInflater layoutInfiater = LayoutInflater.from(context);
            convertView = layoutInfiater.inflate(R.layout.list_adapter_view, null);

            viewItem.txtTitle = (TextView)convertView.findViewById(R.id.adapter_text_title);
            viewItem.txtDescription = (TextView)convertView.findViewById(R.id.adapter_text_description);
            convertView.setTag(viewItem);
        }
        else
        {
            viewItem = (ViewItem) convertView.getTag();
        }

        viewItem.txtTitle.setText(valueList.get(position).fire_location);
        viewItem.txtDescription.setText(valueList.get(position).fire_description);

        return convertView;
    }
}

class ViewItem
{
    TextView txtTitle;
    TextView txtDescription;
}


