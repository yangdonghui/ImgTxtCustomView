package sample.bakerj.imgtxtcustomview;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

public class AdapterViewActivity extends Activity {
	
	private GridView gridView;
	private GridViewAdapter adapter;
	private LayoutInflater inflater;
	private int[] srcs = {
		R.mipmap.apple_itunes, R.mipmap.bitlord, R.mipmap.chat, R.mipmap.clef,
		R.mipmap.clock, R.mipmap.contacts, R.mipmap.corel, R.mipmap.documents1,
		R.mipmap.extra, R.mipmap.fallout, R.mipmap.goo, R.mipmap.i_explorer,
		R.mipmap.network_places, R.mipmap.paint, R.mipmap.rtm, R.mipmap.stellarium,
		R.mipmap.ventrilo, R.mipmap.where, R.mipmap.win_media, R.mipmap.winrar
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_adapterview);
		
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		gridView = (GridView) findViewById(R.id.gridview);
		adapter = new GridViewAdapter();
		gridView.setAdapter(adapter);
	}
	
	private class GridViewAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return srcs.length;
		}

		@Override
		public Object getItem(int position) {
			return srcs[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.item, null);
				holder.customView = (CustomView) convertView.findViewById(R.id.customview);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.customView.setImageBitmap(BitmapFactory.decodeResource(getResources(), srcs[position]));
			holder.customView.setTitleText("�� " + position +" ��һ������");
			holder.customView.setSubTitleText("�� " + position +" ����������");
			return convertView;
		}
		
	}
	static class ViewHolder {
		CustomView customView;
	}
}
