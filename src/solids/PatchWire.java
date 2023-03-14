package solids;

import transforms.Bicubic;

public class PatchWire extends Solid {

    public PatchWire() {
        //TODO: řídící body

        //Bicubic bicubic = new Bicubic();

        for (int i = 1; i <= 100; i++) {
            double t = 1 / 100.;

            for (int j = 1; i <= 100; i++) {
                double u = j / 100.;

                //vertex = bicubic.compute(u.t);


            }
        }
    }
}
