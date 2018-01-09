package Utilities;

/**
 * Created by n00b_user on 19/10/2017.
 */
public class Weapon implements Comparable <Weapon>{

    //Nombre del arma
    private String name;

    //Poder asociado al arma
    private int damage;

    //PRE:
    //POST: Crea e inicializa un arma algo floja
    public Weapon(){
        name = "Default";
        damage = 0;
    }

    //PRE:
    //POST: Crea e inicializa un arma poderosa y fantastica (al menos mas que el arma creada por defecto)
    public Weapon(String name, int damage){
        this.name = name;
        this.damage = damage;
    }

    //PRE: Weapon bien creada e inicializada
    //POST: Devuelve el daño del arma
    public int getDamage(){
        return damage;
    }

    //PRE: Weapon bien creada e inicializada
    //POST: Devuelve el nombre del arma
    public String getName(){
        return name;
    }

    //PRE: Weapon bien creada e inicializada
    //POST: Asocia un poder al arma, se usa en las sumas de poder de los personajes
    public void setDamage(int damage){
        this.damage = damage;
    }


    @Override
    public boolean equals(Object obj){
        return obj instanceof Weapon && ((Weapon) obj).getName().equals(this.name);
    }

    @Override
    public int compareTo(Weapon w){
        return this.name.compareTo(w.name);
    }

}
