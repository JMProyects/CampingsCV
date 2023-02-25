package jacques.raul.uv.es;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;

public class CampingsAdapter extends RecyclerView.Adapter<CampingsAdapter.ViewHolder>{
    private ArrayList<Camping> campings;
    Context context;

    public CampingsAdapter(ArrayList<Camping> campings, Context context)
    {
        this.context = context;
        this.campings = campings ;
    }
    @Override
    public int getItemCount() {
        return campings.size();
    }
    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public CampingsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int
            viewType) {
        View view =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.camping_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CampingsAdapter.ViewHolder holder, int
            position) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        final Camping camping = campings.get(position);
        holder.campingName.setText(camping.getNombre());
        holder.campingCategoria.setText(camping.getCategoria());
        holder.campingMunicipio.setText(camping.getMunicipio());
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView campingName, campingCategoria, campingMunicipio;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            campingName = (TextView) itemView.findViewById(R.id.campingName);
            campingCategoria = (TextView) itemView.findViewById(R.id.campingCategoria);
            campingMunicipio = (TextView) itemView.findViewById(R.id.campingMunicipio);
        }
    }



}
