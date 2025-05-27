import os
import pickle
from feature_extractor_and_index import CombinedFeatureExtractor
from annoy import AnnoyIndex

def build_index(database_folder, index_path='combined_index.ann', meta_path='filenames.pkl', n_trees=10, device='cpu'):
    extractor = CombinedFeatureExtractor(device=device)
    filenames = []
    embeddings = []

    for fname in os.listdir(database_folder):
        fpath = os.path.join(database_folder, fname)
        if not os.path.isfile(fpath):
            continue
        feat = extractor.get_feature(fpath)
        if feat is None:
            continue
        embeddings.append(feat)
        filenames.append(fname)
        print(f"Đã xử lý: {fname}")

    if not embeddings:
        print("Không có ảnh nào được xử lý!")
        return

    dim = embeddings[0].shape[0]  # 2072
    index = AnnoyIndex(dim, 'euclidean')
    for i, emb in enumerate(embeddings):
        index.add_item(i, emb)

    print("Đang xây dựng AnnoyIndex...")
    index.build(n_trees)
    index.save(index_path)
    print(f"Đã lưu index vào {index_path}")

    with open(meta_path, 'wb') as f:
        pickle.dump(filenames, f)
    print(f"Đã lưu danh sách file vào {meta_path}")

if __name__ == '__main__':
    db_folder = 'D:/WEB_DEMO/src/main/resources/static/font/images'.strip()
    build_index(database_folder=db_folder)
