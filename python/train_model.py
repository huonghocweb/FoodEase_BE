import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestRegressor
from sklearn.metrics import mean_absolute_error
import joblib
import pyodbc

# Kết nối đến SQL Server và lấy dữ liệu
conn = pyodbc.connect("Driver={SQL Server};Server=localhost;Database=FoodEase;UID=sa;PWD=123456;")
query = "SELECT distance, weather, order_time, order_volume, delivery_time FROM delivery_data"
data = pd.read_sql(query, conn)

# Chuẩn bị dữ liệu cho mô hình
X = data[['distance', 'weather', 'order_time', 'order_volume']]
y = data['delivery_time']

# Chia dữ liệu thành tập train và test
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# Huấn luyện mô hình
model = RandomForestRegressor()
model.fit(X_train, y_train)

# Đánh giá mô hình
predictions = model.predict(X_test)
print(f"MAE: {mean_absolute_error(y_test, predictions)}")

# Lưu mô hình
joblib.dump(model, 'delivery_time_model.pkl')
