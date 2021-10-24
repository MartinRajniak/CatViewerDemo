import SwiftUI

struct CatsFilter: View {
    @Environment(\.presentationMode) var presentationMode
    
    @StateObject var store: CatsStore
    
    var body: some View {
        ZStack {
            VStack(
                alignment: .leading,
                spacing: 32
                
            ) {
                List {
                    Text("Categories")
                    
                    ForEach(store.categories.indices, id: \.self) { index in
                        Toggle(isOn: $store.categories[index].enabled) {
                            Text(store.categories[index].name)
                        }
                        .onChange(of: store.categories[index].enabled, perform: { checked in
                            store.onCategoryChecked(
                                categoryId: store.categories[index].id,
                                checked: checked
                            )
                        })
                    }
                    
                    Spacer()
                    
                    Text("MimeTypes")
                    
                    ForEach(store.mimeTypes.indices, id: \.self) { index in
                        Toggle(isOn: $store.mimeTypes[index].enabled) {
                            Text(store.mimeTypes[index].name)
                        }
                        .onChange(of: store.mimeTypes[index].enabled, perform: { checked in
                            store.onMimeTypeChecked(
                                mimeTypeId: store.mimeTypes[index].id,
                                checked: checked
                            )
                        })
                    }
                }
            }
            .navigationTitle("Filter")
            .toolbar {
                ToolbarItem(placement: .navigationBarTrailing) {
                    Button(action: close) {
                        Image(systemName: "checkmark")
                    }
                }
            }
        }
    }
}

extension CatsFilter {
    func close() {
        presentationMode.wrappedValue.dismiss()
    }
}

struct CatsFilter_Previews: PreviewProvider {
    static var previews: some View {
        CatsFilter(store: CatsStore())
    }
}
