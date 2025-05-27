import torch
import torchvision
import torchvision.transforms as T
import numpy as np
from PIL import Image
import cv2

class CombinedFeatureExtractor:
    def __init__(self, device='cpu'):
        self.model = torchvision.models.resnet50(pretrained=True)
        self.model = torch.nn.Sequential(*(list(self.model.children())[:-1]))
        self.model.eval()
        self.device = device
        self.model.to(self.device)

        self.transform = T.Compose([
            T.Resize(256),
            T.CenterCrop(224),
            T.ToTensor(),
            T.Normalize(mean=[0.485, 0.456, 0.406],
                        std=[0.229, 0.224, 0.225])
        ])

    def get_cnn_embedding(self, img):
        img_tensor = self.transform(img).unsqueeze(0).to(self.device)
        with torch.no_grad():
            feat = self.model(img_tensor)
        feat = feat.squeeze().cpu().numpy()
        norm = np.linalg.norm(feat)
        if norm != 0:
            feat = feat / norm
        return feat

    def get_color_histogram(self, img, bins=8):
        img_np = np.array(img)
        hsv = cv2.cvtColor(img_np, cv2.COLOR_RGB2HSV)
        hist_features = []
        for i in range(3):
            hist = cv2.calcHist([hsv], [i], None, [bins], [0, 256])
            hist = cv2.normalize(hist, hist).flatten()
            hist_features.extend(hist)
        hist_features = np.array(hist_features)
        norm = np.linalg.norm(hist_features)
        if norm != 0:
            hist_features = hist_features / norm
        return hist_features

    def get_feature(self, image_path):
        try:
            img = Image.open(image_path).convert('RGB')
        except Exception as e:
            print(f"Lỗi mở ảnh {image_path}: {e}")
            return None
        cnn_feat = self.get_cnn_embedding(img)
        color_feat = self.get_color_histogram(img, bins=8)
        combined = np.concatenate([cnn_feat, color_feat])
        norm = np.linalg.norm(combined)
        if norm != 0:
            combined = combined / norm
        return combined