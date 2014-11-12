/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package funciones;

import java.util.Objects;

/**
 *
 * @author diego
 */
public class Pair<L,R> {
    private L l;
    private R r;
    
    public Pair(L l, R r){
        this.l = l;
        this.r = r;
    }

      public L getL() {
            return l;
      }

      public void setL(L l) {
            this.l = l;
      }

      public R getR() {
            return r;
      }

      public void setR(R r) {
            this.r = r;
      }

      @Override
      public int hashCode() {
            int hash = 5;
            return hash;
      }

      @Override
      public boolean equals(Object obj) {
            if (obj == null) {
                  return false;
            }
            if (getClass() != obj.getClass()) {
                  return false;
            }
            final Pair<?, ?> other = (Pair<?, ?>) obj;
            if (!Objects.equals(this.l, other.l)) {
                  return false;
            }
            if (!Objects.equals(this.r, other.r)) {
                  return false;
            }
            return true;
      }

      @Override
      public String toString() {
            return "Pair{" + "l=" + l + ", r=" + r + '}';
      }
    
      
      
}
