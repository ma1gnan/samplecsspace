import os
import numpy as np
import matplotlib
matplotlib.use("Agg")
import matplotlib.pyplot as plt
from scipy.io import loadmat
from sklearn.model_selection import train_test_split
from sklearn import svm
from sklearn.metrics import accuracy_score

# Question 1b
# Linear SVM class trained with Batch Gradient Descent
class LinearSVM:
    def __init__(self):
        self.w = None
        self.b = None

    def train(self, X, y, learning_rate=0.001, C=1.0, n_iters=1000, verbose=False):
        """
        X : (n_samples, n_features)
        y : (n_samples,) with values in {-1, +1}
        """
        n_samples, n_features = X.shape
        self.w = np.zeros(n_features)
        self.b = 0.0

        for it in range(n_iters):
            # margin for every sample, vectorized: shape
            margins = y * (X @ self.w + self.b)

            # boolean mask: True where the hinge loss is ACTIVE
            violating = margins < 1

            # gradient of the regularizer term is always w
            grad_w = self.w.copy()
            grad_b = 0.0

            # add contribution from violating samples 
            if np.any(violating):
                grad_w += C * (-(y[violating, None] * X[violating]).sum(axis=0))
                grad_b += C * (-y[violating].sum())

            # batch gradient descent update
            self.w -= learning_rate * grad_w
            self.b -= learning_rate * grad_b

            if verbose and it % 100 == 0:
                hinge_loss = np.maximum(0, 1 - margins).sum()
                obj = 0.5 * np.dot(self.w, self.w) + C * hinge_loss
                print(f"iter {it:4d}  objective = {obj:.4f}")

        return self

    def decision_function(self, X):
        return X @ self.w + self.b

    def predict(self, X):
        return np.sign(self.decision_function(X))


# decision-boundary plot 
def display_decision_boundary(X, y, model, title=None, save_path=None):
    plt.figure(figsize=(6, 5))
    plt.scatter(X[:, 0], X[:, 1], c=y, cmap='bwr', edgecolors='k')
    ax = plt.gca()
    xlim = ax.get_xlim()
    ylim = ax.get_ylim()

    xx, yy = np.meshgrid(np.linspace(xlim[0], xlim[1], 50),
                          np.linspace(ylim[0], ylim[1], 50))
    xy = np.vstack([xx.ravel(), yy.ravel()]).T
    Z = model.predict(xy).reshape(xx.shape)
    ax.contourf(xx, yy, Z, alpha=0.3, cmap='bwr')
    if title:
        plt.title(title)
    plt.xlabel("x1")
    plt.ylabel("x2")
    plt.tight_layout()
    if save_path:
        plt.savefig(save_path, dpi=120)
        plt.close()
    else:
        plt.show()


# QUESTION 1c & 1d 
# Load data1
def run_question1(data_path="hw3data1.mat", out_dir="."):
    data1 = loadmat(data_path)
    X = data1['X']
    y = data1['y'].squeeze().astype(float)
    # make sure labels are -1/+1 (some .mat files store 0/1)
    if set(np.unique(y)) == {0, 1}:
        y = np.where(y == 0, -1, 1)

    # plot raw data
    plt.figure(figsize=(6, 5))
    plt.scatter(X[:, 0], X[:, 1], c=y, cmap='bwr', edgecolors='k')
    plt.title("HW3 Data 1 - raw data")
    plt.xlabel("x1"); plt.ylabel("x2")
    plt.tight_layout()
    plt.savefig(os.path.join(out_dir, "q1_raw_data.png"), dpi=120)
    plt.close()

    # 80/20 split
    X_train, X_test, y_train, y_test = train_test_split(
        X, y, test_size=0.2, random_state=42, stratify=y)

    # sweep C values
    results = []
    for C in [10, 50, 100, 1000]:
        model = LinearSVM()
        model.train(X_train, y_train, learning_rate=0.001, C=C, n_iters=1000)

        # evaluate on test set
        y_pred = model.predict(X_test)
        acc = accuracy_score(y_test, y_pred)
        print(f"C = {C:5d}  ->  test accuracy = {acc:.4f}  w = {model.w}  b = {model.b:.4f}")
        results.append({"C": C, "test_acc": acc, "w": model.w.copy(), "b": model.b})

        # visualize on the full dataset, as instructed
        display_decision_boundary(X, y, model, title=f"Linear SVM decision boundary, C={C}",
                                   save_path=os.path.join(out_dir, f"q1_boundary_C{C}.png"))
    return results


# QUESTION 2b 
# Gaussian Kernel implementation
def my_gaussian_kernel(x1, x2, sigma):
    x1 = np.asarray(x1, dtype=float)
    x2 = np.asarray(x2, dtype=float)
    sq_dist = np.sum((x1 - x2) ** 2)
    return np.exp(-sq_dist / (2 * sigma ** 2))


def test_gaussian_kernel():
    x1 = np.array([1, 2, 1])
    x2 = np.array([0, 4, -1])
    sigma = 2
    sim = my_gaussian_kernel(x1, x2, sigma)
    print(f"my_gaussian_kernel(x1, x2, sigma=2) = {sim:.6f}")
    return sim


# QUESTION 2
# Non-linear SVM with sklearn RBF kernel, gamma search
def run_question2(data_path="hw3data2.mat", out_dir="."):
    data2 = loadmat(data_path)
    X = data2['X']
    y = data2['y'].squeeze()

    # plot raw data
    plt.figure(figsize=(6, 5))
    plt.scatter(X[:, 0], X[:, 1], c=y, cmap='bwr', edgecolors='k')
    plt.title("HW3 Data 2 - raw data")
    plt.xlabel("x1"); plt.ylabel("x2")
    plt.tight_layout()
    plt.savefig(os.path.join(out_dir, "q2_raw_data.png"), dpi=120)
    plt.close()

    # splitting the data into training and testing sets
    X_train, X_test, y_train, y_test = train_test_split(
        X, y, test_size=0.2, random_state=42, stratify=y)

    # gamma search - gamma = 1 / (2 * sigma^2)
    gamma_values = [1, 5, 10, 25, 50, 100]
    results = []
    best_acc, best_gamma, best_model = -1, None, None

    # sweep gamma values
    for gamma in gamma_values:
        model = svm.SVC(kernel="rbf", gamma=gamma, C=1)
        model.fit(X_train, y_train)
        y_pred = model.predict(X_test)
        acc = accuracy_score(y_test, y_pred)
        print(f"gamma = {gamma:5d}  ->  test accuracy = {acc:.4f}")
        results.append({"gamma": gamma, "test_acc": acc})

        display_decision_boundary(X, y, model, title=f"RBF SVM decision boundary, gamma={gamma}",
                                   save_path=os.path.join(out_dir, f"q2_boundary_gamma{gamma}.png"))

        if acc > best_acc:
            best_acc, best_gamma, best_model = acc, gamma, model

    print(f"\nBest gamma = {best_gamma}, test accuracy = {best_acc:.4f}")
    return best_model, best_gamma, best_acc, results


if __name__ == "__main__":
    print("=== Testing Gaussian Kernel (Q2b) ===")
    test_gaussian_kernel()

    print("\n=== Question 1: Linear SVM ===")
    run_question1("hw3data1.mat", out_dir=".")

    print("\n=== Question 2: Non-linear SVM ===")
    run_question2("hw3data2.mat", out_dir=".")
