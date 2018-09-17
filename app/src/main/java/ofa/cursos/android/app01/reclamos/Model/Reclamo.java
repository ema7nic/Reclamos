package ofa.cursos.android.app01.reclamos.Model;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

public class Reclamo {
    private Integer id;
    private String descripcion;
    private LatLng ubicacion;
    private Boolean resuelto;
    private String mailContacto;
    private String pathImagen;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LatLng getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(LatLng ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Boolean getResuelto() {
        return resuelto;
    }

    public void setResuelto(Boolean resuelto) {
        this.resuelto = resuelto;
    }

    public String getMailContacto() {
        return mailContacto;
    }

    public void setMailContacto(String mailContacto) {
        this.mailContacto = mailContacto;
    }

    public String getPathImagen() {
        return pathImagen;
    }

    public void setPathImagen(String pathImagen) {
        this.pathImagen = pathImagen;
    }

    public JSONObject toJson() {

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("id", this.getId());
            jsonObject.put("descripcion", this.getDescripcion());
            jsonObject.put("latitud", String.valueOf(this.getUbicacion().latitude));
            jsonObject.put("longitud", String.valueOf(this.getUbicacion().longitude));
            jsonObject.put("resuelto", this.getResuelto());
            jsonObject.put("email", this.getMailContacto());
            jsonObject.put("imagen", this.getPathImagen());
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public void loadFromJson(JSONObject fila)
    {
        try {
            this.setDescripcion(fila.getString("descripcion"));
            this.setUbicacion(new LatLng(Double.valueOf(fila.getString("latitud")),Double.valueOf(fila.getString("longitud"))));
            this.setResuelto(fila.getBoolean("resuelto"));
            this.setPathImagen(fila.getString("imagen"));
            this.setMailContacto(fila.getString("email"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
