import numpy as np
import matplotlib.pyplot as plt


data = np.loadtxt("hw2data.txt", delimiter=',')

X = data[:, :2]          # features (size and number of bedrooms)
y = data[:, 2:3]         # target variable (price)

m = y.shape[0]
print("First 10 examples from the dataset:")
for i in range(10):
    print(f"  x = {X[i]}, y = {y[i, 0]}")


# this function normalize the data by subtracting mean and dividing by the standard deviation
def featureNormalize(X):

    mu = np.mean(X, axis=0, keepdims=True)
    sigma = np.std(X, axis=0, keepdims=True)
    X_norm = (X - mu) / sigma
    return X_norm, mu, sigma

# A call to the function
X_norm, mu, sigma = featureNormalize(X)

# Add column of 1's 
m_examples = X_norm.shape[0]
X_norm = np.hstack((np.ones((m_examples, 1)), X_norm))

print("\nShape check after adding bias column:")
print("X shape:", X_norm.shape, " y shape:", y.shape)


# A function to compute cost
def computeCost(X, y, theta):

    m = y.shape[0]
    errors = X @ theta - y
    J = (1.0 / (2 * m)) * (errors.T @ errors)
    return J[0, 0]


# Gradient descent
def gradientDescent(X, y, theta, alpha, num_iters):
    
    m = y.shape[0]
    J_history = np.zeros(num_iters)

    for i in range(num_iters):
        errors = X @ theta - y
        gradient = (1.0 / m) * (X.T @ errors)
        theta = theta - alpha * gradient
        J_history[i] = computeCost(X, y, theta)

    return theta, J_history

# implement gradient descent
alphas = [0.01, 0.03, 0.2, 0.4]
num_iters = 50

plt.figure(figsize=(8, 6))
for alpha in alphas:
    theta_init = np.zeros((3, 1))
    _, J_history = gradientDescent(X_norm, y, theta_init, alpha, num_iters)
    plt.plot(range(1, num_iters + 1), J_history, label=f"alpha = {alpha}")

plt.xlabel("Number of iterations")
plt.ylabel("Cost J")
plt.title("Convergence of gradient descent for different learning rates")
plt.legend()
plt.savefig("cost_history_alphas.png", dpi=150)
plt.close()
print("\nSaved plot comparing learning rates to cost_history_alphas.png")

best_alpha = 0.1          
num_iters_final = 400      

theta = np.zeros((3, 1))
theta, J_history = gradientDescent(X_norm, y, theta, best_alpha, num_iters_final)

plt.figure(figsize=(8, 6))
plt.plot(range(1, num_iters_final + 1), J_history)
plt.xlabel("Number of iterations")
plt.ylabel("Cost J")
plt.title(f"Cost history (alpha = {best_alpha})")
plt.savefig("cost_history_final.png", dpi=150)
plt.close()

print(f"\nTheta learned by gradient descent (alpha={best_alpha}, iters={num_iters_final}):")
print(theta.ravel())

# Predict price of a 1650 sq-ft, 3-bedroom house
x_test = np.array([[1650, 3]])
x_test_norm = (x_test - mu) / sigma
x_test_norm = np.hstack((np.ones((1, 1)), x_test_norm))
price_gd = (x_test_norm @ theta)[0, 0]
print(f"\nPredicted price (gradient descent) for a 1650 sq-ft, 3-bedroom house: ${price_gd:,.2f}")

# (f) Normal equations
def normalEqn(X, y):
    """
    Computes the closed-form solution to linear regression:
    theta = (X'X)^-1 X'y
    """
    theta = np.linalg.inv(X.T @ X) @ X.T @ y
    return theta


X_raw = np.hstack((np.ones((m, 1)), X))   
theta_normal = normalEqn(X_raw, y)

print("\nTheta learned by normal equations:")
print(theta_normal.ravel())

x_test_raw = np.array([[1, 1650, 3]])
price_normal = (x_test_raw @ theta_normal)[0, 0]
print(f"\nPredicted price (normal equations) for a 1650 sq-ft, 3-bedroom house: ${price_normal:,.2f}")