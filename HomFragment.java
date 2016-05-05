package com.mfe.offtopic;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.millennialmedia.android.MMAdView;
import com.millennialmedia.android.MMRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class HomeFragment extends Fragment {

    View rootView;
    List<Map> data;
    List<Map> jdata;
    String barTitle;

    int adRowNumber;
    int rowPostion;



    public HomeFragment(){}



        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        barTitle = getArguments().getString("ActionBarTitle");

            //advertising display row
            adRowNumber = 3;
            rowPostion = 0;

            // remove bottom banner
            ((MainActivity) getActivity()).removeBottomBanner();



        if(new MyUtilities().isNetworkAvailable(getActivity()) == false)
        {
            //    Toast.makeText(this, "Attention: No network activity available!", Toast.LENGTH_SHORT).show();
              startActivity(new Intent(getActivity() , Nointernet.class));
            //  finish();
        }

        else
        {
			/* ######################################################  */
            runQuery("new");
			/* ######################################################  */
        }



        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



    }


    @Override
    public void onResume() {
        super.onResume();
        // Set title
        getActivity().getActionBar()
                .setTitle(barTitle);

        //((MainActivity)getActivity()).getActionBar().setTitle("Home");
    }



    // run query
    public void runQuery(String Orderby)
    {


           /* ######################################################  */
            new MyAsyncTask().execute("" + Orderby);
			/* ######################################################  */




    }






    public class MyAsyncTask extends AsyncTask<String, Integer, List<Map>> {

        private ProgressDialog dialog;

        public MyAsyncTask() {
            // TODO Auto-generated constructor stub
            dialog = new ProgressDialog(getActivity());
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("loading data ... please wait!");

        }


        @Override
        protected List<Map> doInBackground(String... params) {
            // TODO Auto-generated method stub
            // Log.i("MMMMMMMMMM:"," 0000000000" + getListingsData(cid));
            int cid = 0;
            if(jdata != null && (jdata.size() > 0)) {
                return jdata;
            }
            return getShowData(cid, params[0]);
        }


        @Override
        protected void onPostExecute(final List<Map> result) {
            // TODO Auto-generated method stub

            //Log.i("MMMMMMMMMM:"," 0000000000" + result.size());

					/* */
            if(!result.isEmpty())
            {
                //set jdata to the result
               jdata = result;

                //Run grid update
                updateGridData(result);


            }


            if (dialog.isShowing()) {
                dialog.dismiss();
            }


        }


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog.show();
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
            dialog.show();
        }


//////////////////////////////////////////
public void updateGridData(final List<Map> result){

       GridView gridView;
       gridView = (GridView) rootView.findViewById(R.id.gridView);

       gridView.setAdapter(new GridAdapter(HomeFragment.this.getActivity(), result));

       gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view,
                                   int position, long id) {
               //Toast.makeText(getActivity(), String.format("You Clicked at %d", position), Toast.LENGTH_SHORT).show();


               // Call show details
               ((MainActivity) getActivity()).callShowDetails("" + result.get(position).get("ID"),"" + result.get(position).get("ShowName"));

           }
       });


   }







    }












    public List<Map> getShowData(int cid,String sortby){

        // Log.i("onPostExecuteMMMMMMMMMMMMMM:"," 0000000000000000000000getListingsData" );

        JSONObject j = null;
        JSONArray json_dataArray  = null;

        String params=null;



            params="wsdl&method=getShows&OrderBy=" + sortby;





        String urlstring = "" + new Globals().webservice_url + "mobile.cfc";



        try {


            try {
                j = new MyUtilities().getJsonHttpData(urlstring, params);
                //jrecords = j;
            } catch (ClientProtocolException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            json_dataArray = (JSONArray) j.get("DATA");
            // Log.i("Return SubCategoriesIyua","" + json_dataArray);


            //########################################################################################
            JSONArray  json_columnArray  = null;
            json_columnArray = (JSONArray) j.get("COLUMNS");

            HashMap<String, String> columns = new HashMap<String,String>();



            for(int i = 0; i < json_columnArray.length(); i++){
                String value = "" + i;
                String name = "" + json_columnArray.getString(i);
                columns.put(name, value);
            }


            //########################################################################################
            // Log.i("Return COLUMNS"," " + json_columnArray );
            // Log.i("Return SubCategories"," " + json_columnArray.getString(0));




            List<Map> list = new ArrayList<Map>();

            Map map = new HashMap();
            Bitmap bitmap;
            bitmap = null;

            if(!(json_dataArray.length() > 0))
            {
                // startActivity(new Intent(getApplicationContext(), NoListings.class));

                Log.i("Return CCCOUUUNT", " " + json_dataArray.length());
            }

            // Log.i("Return columns "," " + columns );

            for(int i=0;i<json_dataArray.length();i++){



                map = new HashMap();



                map.put("ID", json_dataArray.getJSONArray(i).getString(0));
                map.put("Icon", R.drawable.clear100);
                map.put("ShowName", json_dataArray.getJSONArray(i).getString(Integer.parseInt(columns.get("NAME"))));

                map.put("ShowRating", "Rated: " + json_dataArray.getJSONArray(i).getString(Integer.parseInt(columns.get("RATING"))));
                map.put("ShowDuration", json_dataArray.getJSONArray(i).getString(Integer.parseInt(columns.get("RUNTIME"))));
                map.put("ShowViews",  json_dataArray.getJSONArray(i).getString(Integer.parseInt(columns.get("VIEWS"))) + " Views" );
              //  map.put("Title", json_dataArray.getJSONArray(i).getString(Integer.parseInt(columns.get("NAME"))));



                try {
                    //  bitmap = new DownloadImageTask().execute("" + json_dataArray.getJSONArray(i).getString(Integer.parseInt(columns.get("PICTURES"))).trim()).get();
                    // Log.i("ME EErrori ", "" + json_dataArray.getJSONArray(i).getString(Integer.parseInt(columns.get("IMAGETHUMBNAILURL"))));

                 bitmap = downloadAvatar("" + json_dataArray.getJSONArray(i).getString(Integer.parseInt("" + columns.get("PHOTOLINK"))) );

                    map.put("Pictures", bitmap);
                    bitmap = null;
                }
                catch (NumberFormatException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    e.getMessage();
                }

                catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    e.getMessage();
                }




                list.add(map);



/////////////////////////////////////////////////////////////////////////
                // add advertising rows
                if (0 == (i % adRowNumber)) {

                    map = new HashMap();

                    map.put("ID",  "0" );
                    map.put("Icon", R.drawable.clear100);
                    map.put("ShowName",  "" );

                    map.put("ShowRating", "" );
                    map.put("ShowDuration", "" );
                    map.put("ShowViews",  "" );
                    map.put("Pictures", bitmap);

                    list.add(map);

                }

/////////////////////////////////////////////////////////////////////////




            }

            return list;

        }

        catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



        return null;

    }












    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        // ImageView bmImage;

        //public DownloadImageTask(ImageView bmImage) {
        // this.bmImage = bmImage;
        // }

        public DownloadImageTask() {
            // this.bmImage = bmImage;
        }

        public ProgressDialog Dialog = new ProgressDialog(getActivity());



        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            Dialog.setMessage("Processing..");


                    Dialog.show();



        }


        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;

            if(!urldisplay.equals(null))
            {
                try {
                    // InputStream in = new java.net.URL(urldisplay).openStream();
                    // mIcon11 = BitmapFactory.decodeStream(in);
                    mIcon11 = downloadAvatar(urldisplay);
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }

            }
            return mIcon11;
        }




        protected void onPostExecute(Bitmap result) {
            Dialog.dismiss();
            // bmImage.setImageBitmap(result);
        }


    }




    static Bitmap downloadAvatar(String url) {



        final AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
        final HttpGet getRequest = new HttpGet(url);

        try {
            HttpResponse response = client.execute(getRequest);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                Log.w("AvatarDownloader", "Error " + statusCode
                        + " while retrieving bitmap from " + url);
                return null;
            }

            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inputStream = null;
                try {
                    inputStream = entity.getContent();

                    BitmapFactory.Options options=new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    options.inSampleSize =  calculateInSampleSize(options, 320, 180);
                    options.inJustDecodeBounds = false;

                    final Bitmap bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(inputStream,null,options), 320, 180, true);

                    return bitmap;
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    entity.consumeContent();
                }
            }
        } catch (Exception e) {
            // Could provide a more explicit error message for IOException or
            // IllegalStateException
            getRequest.abort();
            Log.w("AvatarDownloader", "Error while retrieving bitmap from "
                    + url);
        } finally {
            if (client != null) {
                client.close();
            }
        }
        return null;
    }







    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
// Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }






}























class ShowItems {
    Bitmap imageId;
    String showID;
    String showName;
    String showRating;
    String showDuration;
    String showViews;

    ShowItems(Bitmap imageId,String showID,String showName,String showRating,String showDuration,String showViews)
    {
        this.imageId = imageId;
        this.showID= showID;
        this.showName = showName;
        this.showRating = showRating;
        this.showDuration = showDuration;
        this.showViews = showViews;
    }

}






class GridAdapter extends BaseAdapter {

    ArrayList<ShowItems> list;
    Context context;
    private List<Map> mData;

    MMAdView adViewFromXml;


    GridAdapter(Context context,List<Map> data){

        this.context = context;
       this.mData = data;

        list = new ArrayList<ShowItems>();
        Resources res = context.getResources();

/*
       int[] tempImages = {R.drawable.dr_shill_still,R.drawable.pilot,R.drawable.jeff_interview,R.drawable.mark_interview,R.drawable.icon_72,R.drawable.icon_72,R.drawable.icon_72,R.drawable.icon_72,R.drawable.icon_72};
       String[] tempShowNames =  res.getStringArray(R.array.nav_drawer_items);
       String[] tempShowRating =  res.getStringArray(R.array.nav_drawer_items);
       String[] tempShowDuration =  res.getStringArray(R.array.nav_drawer_items);
       String[] tempShowViews =  res.getStringArray(R.array.nav_drawer_items);
*/

    for(int i = 0; i < mData.size(); i++){

       // Log.i("DDDDDAAAAAAATA", "" + mData.get(i).get("ShowName"));
        ShowItems tempShowItems = new ShowItems((Bitmap) mData.get(i).get("Pictures"),
                "" + mData.get(i).get("ID"),
                "" + mData.get(i).get("ShowName"),
                "" + mData.get(i).get("ShowRating"),
                "" + mData.get(i).get("ShowDuration"),
                "" + mData.get(i).get("ShowViews")
        );

        list.add(tempShowItems);

    }






    }



    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


public class ViewHolder{

     ImageView myShowImage;
     TextView myShowName;
     TextView myShowRating;
     TextView myShowDuration;
     TextView myShowViews;
     LinearLayout mycontainer_300_250;

     ViewHolder(View v){

         myShowImage = (ImageView) v.findViewById(R.id.imageView);
         myShowName = (TextView) v.findViewById(R.id.showname);

         myShowRating = (TextView) v.findViewById(R.id.showrating);
         myShowDuration = (TextView) v.findViewById(R.id.showduration);
         myShowViews = (TextView) v.findViewById(R.id.showviews);
        //container
        mycontainer_300_250 = (LinearLayout) v.findViewById(R.id.container_300_250);

     }

 }



    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View row = view;
        ViewHolder holder = null;

        ShowItems temp = list.get(i);

        //if advertising view
        if(temp.showID.equalsIgnoreCase("0"))
        {
           row = null;
        }
        else
        {
            row = view;

        }

        if(row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if(temp.showID.equalsIgnoreCase("0")) {
                row = inflater.inflate(R.layout.show_item_advertising, viewGroup, false);
            }
            else{
                row = inflater.inflate(R.layout.show_item, viewGroup, false);
            }

            holder = new ViewHolder(row);
            row.setTag(holder);
        }else{
//////// new ////
         LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.show_item, viewGroup, false);

            holder = new ViewHolder(row);
            row.setTag(holder);
////////new /////
       //  holder = (ViewHolder) row.getTag();
        }

        if(!temp.showID.equalsIgnoreCase("0")) {
            if(holder.myShowName !=null) {

                holder.myShowImage.setImageBitmap(temp.imageId);
                holder.myShowName.setText(temp.showName);
                holder.myShowRating.setText(temp.showRating);
                holder.myShowDuration.setText(temp.showDuration);
                holder.myShowViews.setText(temp.showViews);
            }
        }
        else {
            //MMedia advertising


/////////////////////////////////////////////////////////////////////////////////////
//Set your metadata in the MMRequest object
            MMRequest MMrequest = new MMRequest();

            //Find the ad view for reference

            adViewFromXml = (MMAdView) row.findViewById(R.id.adView);
            adViewFromXml.setApid("" + new Globals().MMBannerRectangle_Code);

            //MMRequest object
            MMRequest request = new MMRequest();

            adViewFromXml.setMMRequest(request);

            adViewFromXml.getAd();
/////////////////////////////////////////////////////////////////////////////////////


        }


        return row;
    }


}

