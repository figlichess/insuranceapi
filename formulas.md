## Vehicle current relative value calculating formula. Formula output is percentage of new car purchase price:
    function(age, mileage) = 102+(−7.967)∗age+(0.8337334)∗(age^2)+(−0.07785488)*(age^3)+(0.002518395)∗(age^4)+(−0.0002236396)∗mileage+(3.669157e−10)∗(mileage^2)+(−1.813681e−16)∗(mileage^3)
        (Formula author: Niina Matikainen, phD thesis "Auton arvon aleneminen iän ja käytön myötä", May 2017);

## Casco annual fee calculation formula:
    function(risks, params) = make_risk(producer) * (Σ(f(risk_coefficient * params)));
    simplified: function() = risk_car_producer * (risk_coefficient1 * parameter1 + risk_coefficient2 * parameter2 + ...));

## Example of casco annual fee calculation, which takes into account only make risk and vehicle age risk:
    casco_annual_fee = data.coefficients.make_coefficients[make] * (data.coefficients.vehicle_age * vehicle_age)
